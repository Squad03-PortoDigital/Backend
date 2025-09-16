package com.squad03.flap.service;

import com.squad03.flap.model.Usuario;
import com.squad03.flap.model.Role;
import com.squad03.flap.model.Cargo;
import com.squad03.flap.repository.UsuarioRepository;
import com.squad03.flap.repository.RoleRepository;
import com.squad03.flap.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CargoRepository cargoRepository;

    // Regex para validação de email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    // Salvar novo usuário
    public Usuario salvar(Usuario usuario) {
        validarUsuario(usuario);

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário com este email");
        }

        validarRoleECargo(usuario);

        return usuarioRepository.save(usuario);
    }

    // Atualizar usuário
    public Usuario atualizar(Usuario usuario) {
        if (usuario.getId() == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }

        if (!usuarioRepository.existsById(usuario.getId())) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        validarUsuario(usuario);

        // Verificar se já existe outro usuário com o mesmo email
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("Já existe um usuário com este email");
        }

        validarRoleECargo(usuario);

        return usuarioRepository.save(usuario);
    }

    // Buscar todos os usuários
    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAllByOrderByNomeAsc();
    }

    // Buscar todos com role e cargo carregados
    @Transactional(readOnly = true)
    public List<Usuario> buscarTodosComRoleECargo() {
        return usuarioRepository.findAllWithRoleAndCargo();
    }

    // Buscar usuário por ID
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Buscar usuário por ID com role e cargo
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorIdComRoleECargo(Long id) {
        return usuarioRepository.findByIdWithRoleAndCargo(id);
    }

    // Buscar usuário por email
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Buscar usuário por email com role e cargo
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmailComRoleECargo(String email) {
        return usuarioRepository.findByEmailWithRoleAndCargo(email);
    }

    // Buscar usuários por nome
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    // Buscar usuários por role
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorRole(Long roleId) {
        return usuarioRepository.findByRoleId(roleId);
    }

    // Buscar usuários por cargo
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorCargo(Long cargoId) {
        return usuarioRepository.findByCargoId(cargoId);
    }

    // Buscar usuários por nome da role
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorNomeRole(String nomeRole) {
        return usuarioRepository.findByRoleNome(nomeRole);
    }

    // Buscar usuários por nome do cargo
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorNomeCargo(String nomeCargo) {
        return usuarioRepository.findByCargoNome(nomeCargo);
    }

    // Buscar usuários com foto
    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuariosComFoto() {
        return usuarioRepository.findUsuariosComFoto();
    }

    // Buscar usuários sem foto
    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuariosSemFoto() {
        return usuarioRepository.findUsuariosSemFoto();
    }

    // Deletar usuário
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    // Verificar se existe
    @Transactional(readOnly = true)
    public boolean existe(Long id) {
        return usuarioRepository.existsById(id);
    }

    // Atualizar foto do usuário
    public Usuario atualizarFoto(Long id, String fotoBase64) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (!usuarioOpt.isPresent()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setFoto(fotoBase64);
        return usuarioRepository.save(usuario);
    }

    // Métodos privados de validação
    private void validarUsuario(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }

        if (!EMAIL_PATTERN.matcher(usuario.getEmail()).matches()) {
            throw new IllegalArgumentException("Email inválido");
        }

        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser vazia");
        }

        if (usuario.getSenha().length() < 6) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
        }
    }

    private void validarRoleECargo(Usuario usuario) {
        if (usuario.getRole() == null || usuario.getRole().getId() == null) {
            throw new IllegalArgumentException("Role é obrigatória");
        }

        if (usuario.getCargo() == null || usuario.getCargo().getId() == null) {
            throw new IllegalArgumentException("Cargo é obrigatório");
        }

        if (!roleRepository.existsById(usuario.getRole().getId())) {
            throw new IllegalArgumentException("Role não encontrada");
        }

        if (!cargoRepository.existsById(usuario.getCargo().getId())) {
            throw new IllegalArgumentException("Cargo não encontrado");
        }
    }
}
package com.squad03.flap.service;

import com.squad03.flap.model.Usuario;
import com.squad03.flap.repository.CargoRepository;
import com.squad03.flap.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private CargoRepository cargoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 🔐 Para codificar senhas

    // Regex para validação de e-mail
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    // Criar novo usuário
    public Usuario salvar(Usuario usuario) {
        validarUsuario(usuario);

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário com este e-mail");
        }

        validarCargo(usuario);

        // 🔐 Codifica a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        return usuarioRepository.save(usuario);
    }

    // Atualizar usuário existente
    public Usuario atualizar(Usuario usuario) {
        if (usuario.getId() == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }

        if (!usuarioRepository.existsById(usuario.getId())) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        validarUsuario(usuario);

        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("Já existe um usuário com este e-mail");
        }

        validarCargo(usuario);

        // 🔐 Se a senha foi alterada, reencoda
        if (!usuario.getSenha().startsWith("$2a$")) { // evita reencodar senha já criptografada
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        return usuarioRepository.save(usuario);
    }

    // Buscar todos os usuários
    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAllByOrderByNomeAsc();
    }

    // Buscar usuário por ID
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Buscar usuário por e-mail
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Buscar usuários por nome
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    // Buscar usuários por cargo (ID)
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorCargo(Long cargoId) {
        return usuarioRepository.findByCargoId(cargoId);
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

    // Verificar existência
    @Transactional(readOnly = true)
    public boolean existe(Long id) {
        return usuarioRepository.existsById(id);
    }

    // Atualizar foto
    public Usuario atualizarFoto(Long id, String fotoBase64) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        usuario.setFoto(fotoBase64);
        return usuarioRepository.save(usuario);
    }

    // ======================
    // MÉTODOS DE VALIDAÇÃO
    // ======================
    private void validarUsuario(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("E-mail não pode ser vazio");
        }

        if (!EMAIL_PATTERN.matcher(usuario.getEmail()).matches()) {
            throw new IllegalArgumentException("E-mail inválido");
        }

        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser vazia");
        }

        if (usuario.getSenha().length() < 6) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
        }
    }

    private void validarCargo(Usuario usuario) {
        if (usuario.getCargo() == null || usuario.getCargo().getId() == null) {
            throw new IllegalArgumentException("Cargo é obrigatório");
        }

        if (!cargoRepository.existsById(usuario.getCargo().getId())) {
            throw new IllegalArgumentException("Cargo não encontrado");
        }
    }
}

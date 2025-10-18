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
    private PasswordEncoder passwordEncoder;

    // Regex para validação de e-mail
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    // ==================== CRIAR NOVO USUÁRIO ====================
    public Usuario salvar(Usuario usuario) {
        validarUsuario(usuario);

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário com este e-mail");
        }

        validarCargo(usuario);

        // 🔐 Codifica a senha APENAS no cadastro
        if (!usuario.getSenha().startsWith("$2a$")) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        return usuarioRepository.save(usuario);
    }

    // ==================== ATUALIZAR USUÁRIO ====================
    public Usuario atualizar(Usuario usuario) {
        if (usuario.getId() == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }

        if (!usuarioRepository.existsById(usuario.getId())) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        // ❌ NÃO valida senha aqui (já está hashada)
        // ❌ NÃO busca senha antiga
        // ✅ Apenas salva a entidade como está

        return usuarioRepository.save(usuario);
    }

    // ==================== ATUALIZAR FOTO ====================
    public Usuario atualizarFoto(Long id, String fotoBase64) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        usuario.setFoto(fotoBase64);

        // ✅ Salva SEM alterar a senha
        return usuarioRepository.save(usuario);
    }

    // ==================== BUSCAR USUÁRIOS ====================

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAllByOrderByNomeAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarPorCargo(Long cargoId) {
        return usuarioRepository.findByCargoId(cargoId);
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarPorNomeCargo(String nomeCargo) {
        return usuarioRepository.findByCargoNome(nomeCargo);
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuariosComFoto() {
        return usuarioRepository.findUsuariosComFoto();
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuariosSemFoto() {
        return usuarioRepository.findUsuariosSemFoto();
    }

    // ==================== DELETAR ====================

    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    // ==================== VERIFICAÇÕES ====================

    @Transactional(readOnly = true)
    public boolean existe(Long id) {
        return usuarioRepository.existsById(id);
    }

    // ==================== MÉTODOS DE VALIDAÇÃO ====================

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

        // ✅ Valida senha APENAS no cadastro (quando a senha vem em texto puro)
        if (usuario.getSenha() != null && !usuario.getSenha().startsWith("$2a$")) {
            if (usuario.getSenha().trim().isEmpty()) {
                throw new IllegalArgumentException("Senha não pode ser vazia");
            }
            if (usuario.getSenha().length() < 4) {
                throw new IllegalArgumentException("Senha deve ter pelo menos 4 caracteres");
            }
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

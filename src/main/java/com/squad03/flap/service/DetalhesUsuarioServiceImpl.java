package com.squad03.flap.service;

import com.squad03.flap.model.Usuario;
import com.squad03.flap.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DetalhesUsuarioServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("🔍 ===== TENTANDO AUTENTICAR =====");
        System.out.println("📧 Email recebido: " + email);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("❌ USUÁRIO NÃO ENCONTRADO NO BANCO!");
                    return new UsernameNotFoundException("Usuário não encontrado com e-mail: " + email);
                });

        System.out.println("✅ Usuário encontrado no banco");
        System.out.println("👤 Nome: " + usuario.getNome());
        System.out.println("🔐 Hash senha (primeiros 20 chars): " + usuario.getSenha().substring(0, Math.min(20, usuario.getSenha().length())));
        System.out.println("👔 Role: " + usuario.getRole());

        if (usuario.getRole() == null) {
            System.out.println("⚠️ ATENÇÃO: ROLE É NULL!");
        }

        String roleName = "ROLE_" + usuario.getRole().name();
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(roleName));

        System.out.println("🎫 Authorities: " + authorities);

        UserDetails userDetails = new User(usuario.getEmail(), usuario.getSenha(), authorities);

        System.out.println("✅ UserDetails criado com sucesso");
        System.out.println("=====================================");

        return userDetails;
    }
}

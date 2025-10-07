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
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com e-mail: " + email));

        // Define o papel do usuário no formato ROLE_*
        String roleName = "ROLE_" + usuario.getRole().name();
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(roleName));

        // Retorna o objeto User do Spring Security com e-mail, senha e permissões
        return new User(usuario.getEmail(), usuario.getSenha(), authorities);
    }
}

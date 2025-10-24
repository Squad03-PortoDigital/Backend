package com.squad03.flap.service;

import com.squad03.flap.model.Permissao;
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

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DetalhesUsuarioServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1. Busca o usuário pelo email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com e-mail: " + email));

        // 2. Mapeia as permissões dinâmicas da Role do usuário
        Collection<GrantedAuthority> authorities = mapPermissoesToAuthorities(usuario);

        // 3. Retorna o objeto UserDetails do Spring Security
        return new User(
                usuario.getEmail(),
                usuario.getSenha(), // Senha já criptografada do banco
                authorities // Coleção de todas as permissões (autoridades)
        );
    }

    private Collection<GrantedAuthority> mapPermissoesToAuthorities(Usuario usuario) {
        // Começa com a Role do usuário (útil para o hasRole('ADMIN'))
        // O nome da Role é prefixado com "ROLE_" (Ex: ROLE_ADMINISTRADOR_MASTER)
        Set<GrantedAuthority> authorities = new java.util.HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().getNome().toUpperCase()));

        if (usuario.getRole().getPermissoes() != null) {
            Set<GrantedAuthority> permissaoAuthorities = usuario.getRole().getPermissoes().stream()
                    .map(Permissao::getNome)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            authorities.addAll(permissaoAuthorities);
        }

        return authorities;
    }
}
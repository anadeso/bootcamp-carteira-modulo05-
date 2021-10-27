package br.com.alura.carteira.infra.security;

import br.com.alura.carteira.dto.LoginFormDto;
import br.com.alura.carteira.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutentitacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
                .findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException(""));
    }

    public String autenticar(LoginFormDto dto) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                dto.getLogin(),
                dto.getSenha()
        );
        authentication = authenticationManager.authenticate(authentication );

        return tokenService.gerarToken(authentication);
        // Autentica
        // Gerar o token
        // Devolver token
    }
}

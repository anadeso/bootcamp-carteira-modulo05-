package br.com.alura.carteira.infra.security;

import br.com.alura.carteira.entities.Usuario;
import br.com.alura.carteira.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VerificacaoTokenFilter extends OncePerRequestFilter {

   private TokenService tokenService;
   private UsuarioRepository usuarioRepository;

    public VerificacaoTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {

        //recuperar token, validar, caso correto liberar requisicao
        String token = httpServletRequest.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        token = token.replace("Bearer ", "");
        boolean tokenValido = tokenService.isValido(token);

        if (tokenValido) {
            Long idUsuario = tokenService.extrairIdUsuario(token);
            Usuario logado = usuarioRepository.carregarPorIdComPerfis(idUsuario).get();
            Authentication authentication = new UsernamePasswordAuthenticationToken(logado, null, logado.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

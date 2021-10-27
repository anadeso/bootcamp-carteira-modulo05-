package br.com.alura.carteira.resources;

import br.com.alura.carteira.dto.LoginFormDto;
import br.com.alura.carteira.dto.TokenDto;
import br.com.alura.carteira.infra.security.AutentitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticacaoResouce {

    @Autowired
    private AutentitacaoService service;

    @PostMapping
    public TokenDto autenticar(@RequestBody @Valid LoginFormDto dto) {
        return new TokenDto(service.autenticar(dto));
    }
}

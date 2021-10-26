package br.com.alura.carteira.resources;

import br.com.alura.carteira.dto.UsuarioDto;
import br.com.alura.carteira.dto.UsuarioFormDto;
import br.com.alura.carteira.services.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/usuarios")
@Api(tags = "Usuarios")
public class UsuarioResource {

    @Autowired
    private UsuarioService service;

    @GetMapping
    @ApiOperation("Listar usuarios")
    public Page<UsuarioDto> listar(@PageableDefault(size = 10) Pageable paginacao) {
        return service.listar(paginacao);
    }

    @PostMapping
    @ApiOperation("Cadastrar usuarios")
    public ResponseEntity<UsuarioDto> cadastrar(@RequestBody @Valid UsuarioFormDto usuarioFormDto) {
        UsuarioDto usuarioDto = service.cadastrar(usuarioFormDto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(usuarioDto.getId()).toUri();
        return ResponseEntity.created(uri).body(usuarioDto);
    }
}

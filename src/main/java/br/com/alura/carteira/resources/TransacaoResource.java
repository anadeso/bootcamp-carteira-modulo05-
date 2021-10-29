package br.com.alura.carteira.resources;

import br.com.alura.carteira.dto.AtualizacaoTransacaoFormDto;
import br.com.alura.carteira.dto.TransacaoDetalhadaDto;
import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;

import br.com.alura.carteira.entities.Usuario;
import br.com.alura.carteira.services.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequestMapping("/transacoes")
public class TransacaoResource {

    @Autowired
    private TransacaoService service;

    @GetMapping
    //@AuthenticationPrincipal spring vai injetar o usuario que esta logando usando essa anotacao
    public Page<TransacaoDto> listar(@PageableDefault(size = 10) Pageable paginacao, @AuthenticationPrincipal Usuario logado) {
        return service.listar(paginacao, logado);
    }

    @PostMapping
    public ResponseEntity<TransacaoDto> cadastrar(@RequestBody @Valid TransacaoFormDto transacaoFormDto,  @AuthenticationPrincipal Usuario logado) {
        TransacaoDto transacaoDto = service.cadastrar(transacaoFormDto, logado);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(transacaoDto.getId()).toUri();
        return ResponseEntity.created(uri).body(transacaoDto);
    }

    @PutMapping
    public ResponseEntity<TransacaoDto> atualizar(@RequestBody @Valid AtualizacaoTransacaoFormDto dto, @AuthenticationPrincipal Usuario logado) {
        TransacaoDto atualizada = service.atualizar(dto, logado);

        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TransacaoDto> delete(@PathVariable @NotNull Long id, @AuthenticationPrincipal Usuario logado) {
        service.delete(id, logado);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacaoDetalhadaDto> buscaPorId(@PathVariable @NotNull Long id, @AuthenticationPrincipal Usuario logado) {
        TransacaoDetalhadaDto dto = service.buscaPoriD(id, logado);
        return ResponseEntity.ok(dto);
    }
}

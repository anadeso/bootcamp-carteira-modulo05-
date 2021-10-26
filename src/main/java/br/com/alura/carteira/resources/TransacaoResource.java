package br.com.alura.carteira.resources;

import br.com.alura.carteira.dto.AtualizacaoTransacaoFormDto;
import br.com.alura.carteira.dto.TransacaoDetalhadaDto;
import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;

import br.com.alura.carteira.entities.Transacao;
import br.com.alura.carteira.services.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
    public Page<TransacaoDto> listar(@PageableDefault(size = 10) Pageable paginacao) {
        return service.listar(paginacao);
    }

    @PostMapping
    public ResponseEntity<TransacaoDto> cadastrar(@RequestBody @Valid TransacaoFormDto transacaoFormDto) {
        TransacaoDto transacaoDto = service.cadastrar(transacaoFormDto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(transacaoDto.getId()).toUri();
        return ResponseEntity.created(uri).body(transacaoDto);
    }

    @PutMapping
    public ResponseEntity<TransacaoDto> atualizar(@RequestBody @Valid AtualizacaoTransacaoFormDto dto) {
        TransacaoDto atualizada = service.atualizar(dto);

        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TransacaoDto> delete(@PathVariable @NotNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacaoDetalhadaDto> buscaPorId(@PathVariable @NotNull Long id) {
        TransacaoDetalhadaDto dto = service.buscaPoriD(id);
        return ResponseEntity.ok(dto);
    }
}

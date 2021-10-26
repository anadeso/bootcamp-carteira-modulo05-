package br.com.alura.carteira.service;

import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;
import br.com.alura.carteira.entities.TipoTransacao;
import br.com.alura.carteira.repositories.TransacaoRepository;
import br.com.alura.carteira.repositories.UsuarioRepository;
import br.com.alura.carteira.services.TransacaoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    @Mock
    public TransacaoRepository transacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    private TransacaoFormDto getTransacaoFormDto() {
        return new TransacaoFormDto(
                "ITSA4",
                new BigDecimal("10.45"),
                120,
                LocalDate.now(),
                TipoTransacao.COMPRA,
                1L
        );
    }

    @Test
    public void deveCadastrarUmaTransacaoService() {
        TransacaoFormDto transacaoFormDto = getTransacaoFormDto();

        TransacaoDto dto = transacaoService.cadastrar(transacaoFormDto);

        Mockito.verify(transacaoRepository).save(Mockito.any());

        Assertions.assertEquals(transacaoFormDto.getTicker(), dto.getTicker());
        Assertions.assertEquals(transacaoFormDto.getPreco(), dto.getPreco());
        Assertions.assertEquals(transacaoFormDto.getQuantidade(), dto.getQuantidade());
        Assertions.assertEquals(transacaoFormDto.getTipoTransacao(), dto.getTipoTransacao());
    }

    @Test
    public void naoDeveCadastrarQuandoIdUsuarioNaoExistir() {
        TransacaoFormDto transacaoFormDto = getTransacaoFormDto();

        Mockito.when(usuarioRepository.getById(transacaoFormDto.getUsuarioId()))
                .thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(IllegalArgumentException.class,
                () ->  transacaoService.cadastrar(transacaoFormDto));
    }

}

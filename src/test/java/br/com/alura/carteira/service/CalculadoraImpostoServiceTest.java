package br.com.alura.carteira.service;

import br.com.alura.carteira.entities.TipoTransacao;
import br.com.alura.carteira.entities.Transacao;
import br.com.alura.carteira.entities.Usuario;
import br.com.alura.carteira.services.CalculadoraImpostoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CalculadoraImpostoServiceTest {

    private CalculadoraImpostoService calculadoraImpostoService;

    private Transacao getTransacao(BigDecimal preco, Integer quantidade, TipoTransacao tipo) {
        return new Transacao(
                120L,
                "BBSE3",
                preco,
                quantidade,
                LocalDate.now(),
                tipo,
                new Usuario(1L, "Rafaela", "rafaela@gmail.com", "12344")
        );
    }

    @BeforeEach
    public void inicializar() {
        calculadoraImpostoService = new CalculadoraImpostoService();
    }

    @Test
    public void transacaoDoTipoCompraNaoDeveriaTerImposto(){
        Transacao transacao = getTransacao(new BigDecimal("30.00"), 10, TipoTransacao.COMPRA);

        BigDecimal imposto = calculadoraImpostoService.calcular(transacao);

        Assertions.assertEquals(BigDecimal.ZERO, imposto);
    }


    @Test
    public void transacaoDoTipoVendaComValorMenorDoQueVinteMilNaoDeveriaTerImposto() {
        Transacao transacao = getTransacao(new BigDecimal("30.00"), 10, TipoTransacao.VENDA);

        BigDecimal imposto = calculadoraImpostoService.calcular(transacao);

        Assertions.assertEquals(BigDecimal.ZERO, imposto);
    }

    @Test
    public  void transacaoDoTipoVendaComValorMaiorQueVinteMilDeveriaTerImposto() {
        Transacao transacao = getTransacao(new BigDecimal("1000.00"), 30, TipoTransacao.VENDA);

        BigDecimal imposto = calculadoraImpostoService.calcular(transacao);

        Assertions.assertEquals(new BigDecimal("4500.00"), imposto);

    }
}

package br.com.alura.carteira.services;

import br.com.alura.carteira.entities.TipoTransacao;
import br.com.alura.carteira.entities.Transacao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculadoraImpostoService {

    //15% IMPOSTO PARA TRANSACOES DO TIPO VENDA COM VALOR SUPERIOR A R$20.000,00
    public BigDecimal  calcular(Transacao transacao) {
        if(transacao.getTipoTransacao() == TipoTransacao.COMPRA) {
            return BigDecimal.ZERO;
        }

        BigDecimal valorTransacao = transacao
                .getPreco()
                .multiply(new BigDecimal(transacao.getQuantidade()));

        if(valorTransacao.compareTo(new BigDecimal(20000)) < 0) {
            return BigDecimal.ZERO;
        }
        
        return valorTransacao
                .multiply(new BigDecimal("0.15"))
                .setScale(2, RoundingMode.HALF_UP);
    }
}


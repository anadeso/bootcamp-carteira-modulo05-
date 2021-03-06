package br.com.alura.carteira.dto;

import br.com.alura.carteira.entities.TipoTransacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Classe TransacaoDto deveolvemos ou retornornamos na API
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoDto {

    private Long id;
    private String ticker;
    private BigDecimal preco;
    private Integer quantidade;
    private TipoTransacao tipoTransacao;
    private BigDecimal imposto;
}

package br.com.alura.carteira.dto;

import br.com.alura.carteira.entities.TipoTransacao;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoFormDto {

    @NotBlank
    @Size(min = 5, max = 6)
    @Pattern(regexp = "[a-zA-Z]{4}[0-9][0-9]?", message = "{transacao.ticker.invalido}")
    private String ticker;

    @NotNull
    @Digits(integer=5, fraction=2)
    private BigDecimal preco;

    @NotNull
    @Min(1)
    private int quantidade;

    @PastOrPresent
    @Column(name = "data")
    private LocalDate dataTransacao;

    @Column(name = "tipo")
    private TipoTransacao tipoTransacao;

    @JsonAlias("usuario_id")
    private Long usuarioId;
}

package br.com.alura.carteira.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString(exclude = { "dataTransacao", "quantidade"})
@AllArgsConstructor()
@NoArgsConstructor
@Entity
@Table(name = "transacoes")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ticker;
    private BigDecimal preco;
    private int quantidade;
    @Column(name = "data")
    private LocalDate dataTransacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoTransacao tipoTransacao;

    @ManyToOne
    private Usuario usuario;

    public Transacao(String ticker, BigDecimal preco, int quantidade, LocalDate dataTransacao, TipoTransacao tipoTransacao, Usuario usuario) {
        this.ticker = ticker;
        this.preco = preco;
        this.quantidade = quantidade;
        this.dataTransacao = dataTransacao;
        this.tipoTransacao = tipoTransacao;
        this.usuario = usuario;
    }

    public void atualizarInformacoes(String ticker, BigDecimal preco, int quantidade, LocalDate dataTransacao, TipoTransacao tipoTransacao) {
        this.ticker = ticker;
        this.preco = preco;
        this.quantidade = quantidade;
        this.dataTransacao = dataTransacao;
        this.tipoTransacao = tipoTransacao;
    }
}

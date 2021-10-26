package br.com.alura.carteira.repository;

import br.com.alura.carteira.dto.ItemCarteiraDto;
import br.com.alura.carteira.entities.TipoTransacao;
import br.com.alura.carteira.entities.Transacao;
import br.com.alura.carteira.entities.Usuario;
import br.com.alura.carteira.repositories.TransacaoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TransacaoRepositoryTest {

    @Autowired
    private TransacaoRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void deveriaRetornarRelatorioDeCarteiraDeInvestimentos() {
        Usuario usuario = new Usuario("Rafaela", "rafa@gmail.com", "123456");
        testEntityManager.persist(usuario);

        Transacao transacao1 = new Transacao("ITSA4",
                new BigDecimal("10.00"),
                50,
                LocalDate.now(),
                TipoTransacao.COMPRA,
                usuario);
        testEntityManager.persist(transacao1);

        Transacao transacao2 = new Transacao("bbse3",
                new BigDecimal("22.80"),
                80,
                LocalDate.now(),
                TipoTransacao.COMPRA,
                usuario);
        testEntityManager.persist(transacao2);

        Transacao transacao3 = new Transacao("EGIE3",
                new BigDecimal("40.00"),
                25,
                LocalDate.now(),
                TipoTransacao.COMPRA,
                usuario);
        testEntityManager.persist(transacao3);

        Transacao transacao4 = new Transacao("ITSA4",
                new BigDecimal("11.20"),
                40,
                LocalDate.now(),
                TipoTransacao.COMPRA,
                usuario);
        testEntityManager.persist(transacao4);

        Transacao transacao5 = new Transacao("SAPR4",
                new BigDecimal("04.02"),
                120,
                LocalDate.now(),
                TipoTransacao.COMPRA,
                usuario);
        testEntityManager.persist(transacao5);

        List<ItemCarteiraDto> itemCarteiraDtos = repository.relatorioCarteiraDeInvetimento();

        Assertions
                .assertThat(itemCarteiraDtos)
                .hasSize(4)
                .extracting(ItemCarteiraDto::getTicker, ItemCarteiraDto::getQuantidade, ItemCarteiraDto::getPercentual)
                .containsExactlyInAnyOrder(
                        Assertions.tuple("ITSA4", 90l, new BigDecimal("28.57")),
                        Assertions.tuple("bbse3", 80l, new BigDecimal("25.40")),
                        Assertions.tuple("EGIE3", 25l, new BigDecimal("7.94")),
                        Assertions.tuple("SAPR4", 120l, new BigDecimal("38.10")));

    }

    @Test
    public void deveriaRetornarRelatorioDeCarteiraDeInvestimentoQuandoVenda() {
        Usuario usuario = new Usuario("Rafaela", "rafa@gmail.com", "123456");
        testEntityManager.persist(usuario);

        Transacao transacao1 = new Transacao("ITSA4",
                new BigDecimal("10.00"),
                50,
                LocalDate.now(),
                TipoTransacao.COMPRA,
                usuario);
        testEntityManager.persist(transacao1);

        Transacao transacao2 = new Transacao("bbse3",
                new BigDecimal("22.80"),
                80,
                LocalDate.now(),
                TipoTransacao.COMPRA,
                usuario);
        testEntityManager.persist(transacao2);

        Transacao transacao3 = new Transacao("EGIE3",
                new BigDecimal("40.00"),
                25,
                LocalDate.now(),
                TipoTransacao.COMPRA,
                usuario);
        testEntityManager.persist(transacao3);

        Transacao transacao4 = new Transacao("ITSA4",
                new BigDecimal("11.20"),
                40,
                LocalDate.now(),
                TipoTransacao.VENDA,
                usuario);
        testEntityManager.persist(transacao4);

        Transacao transacao5 = new Transacao("SAPR4",
                new BigDecimal("04.02"),
                120,
                LocalDate.now(),
                TipoTransacao.COMPRA,
                usuario);
        testEntityManager.persist(transacao5);

        List<ItemCarteiraDto> itemCarteiraDtos = repository.relatorioCarteiraDeInvetimento();

        Assertions
                .assertThat(itemCarteiraDtos)
                .hasSize(4)
                .extracting(ItemCarteiraDto::getTicker, ItemCarteiraDto::getQuantidade, ItemCarteiraDto::getPercentual)
                .containsExactlyInAnyOrder(
                        Assertions.tuple("ITSA4", 10l, new BigDecimal("3.17")),
                        Assertions.tuple("bbse3", 80l, new BigDecimal("25.40")),
                        Assertions.tuple("EGIE3", 25l, new BigDecimal("7.94")),
                        Assertions.tuple("SAPR4", 120l,  new BigDecimal("38.10")));
    }
}

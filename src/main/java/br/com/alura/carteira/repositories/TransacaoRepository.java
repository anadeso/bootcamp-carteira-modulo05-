package br.com.alura.carteira.repositories;

import br.com.alura.carteira.dto.ItemCarteiraDto;
import br.com.alura.carteira.entities.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query("select new br.com.alura.carteira.dto.ItemCarteiraDto"
            + "("
            + "t.ticker, "
            + "sum(case when(t.tipoTransacao='COMPRA') then t.quantidade else (t.quantidade * -1) end), "
            + "(select sum(case when(t2.tipoTransacao='COMPRA') then t2.quantidade else (t2.quantidade) end) from Transacao t2)"
            + ") "
            + "from Transacao t "
            + "group by t.ticker"
            )
    List<ItemCarteiraDto> relatorioCarteiraDeInvetimento();
}

package br.com.alura.carteira.services;

import br.com.alura.carteira.dto.ItemCarteiraDto;
import br.com.alura.carteira.repositories.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Transactional(readOnly = true)
    public List<ItemCarteiraDto> relatorioCarteiraDeInvetimento() {
        return transacaoRepository.relatorioCarteiraDeInvetimento();
    }
}

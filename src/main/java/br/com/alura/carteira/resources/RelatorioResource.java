package br.com.alura.carteira.resources;

import br.com.alura.carteira.dto.ItemCarteiraDto;
import br.com.alura.carteira.services.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioResource {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/carteira")
    public List<ItemCarteiraDto> relatorioCarteiraDeInvestimentos() {
        return relatorioService.relatorioCarteiraDeInvetimento();
    }
}

package br.com.alura.carteira.services;

import br.com.alura.carteira.dto.AtualizacaoTransacaoFormDto;
import br.com.alura.carteira.dto.TransacaoDetalhadaDto;
import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;
import br.com.alura.carteira.entities.Transacao;
import br.com.alura.carteira.entities.Usuario;
import br.com.alura.carteira.repositories.TransacaoRepository;
import br.com.alura.carteira.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CalculadoraImpostoService calculadoraImpostoService;

    @Transactional(readOnly = true)
    public Page<TransacaoDto> listar(Pageable paginacao, Usuario usuario) {
        return transacaoRepository
                .findAllByUsuario(paginacao, usuario)
                .map(t -> modelMapper.map(t, TransacaoDto.class));
//        List<TransacaoDto> transacaoDtoList = new ArrayList<>();
//
//        transacoes.forEach(transacao -> {
//            BigDecimal imposto = calculadoraImpostoService.calcular(transacao);
//            TransacaoDto dto = modelMapper.map(transacao, TransacaoDto.class);
//            dto.setImposto(imposto);
//            transacaoDtoList.add(dto);
//        });
//
//        return new PageImpl<TransacaoDto>(
//                transacaoDtoList,
//                transacoes.getPageable(),
//                transacoes.getTotalElements());
    }

    @Transactional
    public TransacaoDto cadastrar(TransacaoFormDto transacaoFormDto, Usuario logado) {
        Long idUsuario = transacaoFormDto.getUsuarioId();

        try {
            Usuario usuario = usuarioRepository.getById(idUsuario);

            // Comparacao de objetos
            if (!usuario.equals(logado)) {
                lancarErroAcessoNegado();
            }
            Transacao transacao = modelMapper.map(transacaoFormDto, Transacao.class);
            transacao.setId(null);
            transacao.setUsuario(usuario);
            BigDecimal imposto = calculadoraImpostoService.calcular(transacao);
            transacao.setImposto(imposto);

            transacaoRepository.save(transacao);

            return modelMapper.map(transacao, TransacaoDto.class);
        } catch (EntityNotFoundException e) {
            throw new IllegalArgumentException("Usuario inexistente!");
        }
    }

    @Transactional
    public TransacaoDto atualizar(AtualizacaoTransacaoFormDto dto, Usuario logado) {
        Transacao transacao = transacaoRepository.getById(dto.getId());

        if (!transacao.pertenceAoUsuario(logado)) {
            return lancarErroAcessoNegado();
        }

        transacao.atualizarInformacoes(dto.getTicker(), dto.getPreco(), dto.getQuantidade(), dto.getDataTransacao(), dto.getTipoTransacao());
        return modelMapper.map(transacao, TransacaoDto.class);
    }

    @Transactional
    public void delete(Long id, Usuario logado) {
        Transacao transacao = transacaoRepository.getById(id);

        if (!transacao.pertenceAoUsuario(logado)) {
            lancarErroAcessoNegado();
        }

        transacaoRepository.deleteById(id);
    }

    @Transactional
    public TransacaoDetalhadaDto buscaPoriD(Long id, Usuario logado) {
        Transacao transacao = transacaoRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        if (!transacao.pertenceAoUsuario(logado)) {
            lancarErroAcessoNegado();
        }

        return modelMapper.map(transacao, TransacaoDetalhadaDto.class);
    }

    private TransacaoDto lancarErroAcessoNegado() {
        throw new AccessDeniedException("Acesso negado");
    }
}

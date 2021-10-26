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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Page<TransacaoDto> listar(Pageable paginacao) {
        Page<Transacao> transacoes = transacaoRepository.findAll(paginacao);
        return transacoes.map(t -> modelMapper.map(t, TransacaoDto.class));
    }

    @Transactional
    public TransacaoDto cadastrar(TransacaoFormDto transacaoFormDto) {
        Long idUsuario = transacaoFormDto.getUsuarioId();

        try {
            Usuario usuario = usuarioRepository.getById(idUsuario);
            Transacao transacao = modelMapper.map(transacaoFormDto, Transacao.class);
            transacao.setId(null);
            transacao.setUsuario(usuario);

            transacaoRepository.save(transacao);

            return modelMapper.map(transacao, TransacaoDto.class);
        } catch (EntityNotFoundException e) {
            throw new IllegalArgumentException("Usuario inexistente!");
        }
    }

    @Transactional
    public TransacaoDto atualizar(AtualizacaoTransacaoFormDto dto) {
        Transacao transacao = transacaoRepository.getById(dto.getId());
        transacao.atualizarInformacoes(dto.getTicker(), dto.getPreco(), dto.getQuantidade(), dto.getDataTransacao(), dto.getTipoTransacao());
        return modelMapper.map(transacao, TransacaoDto.class);
    }

    @Transactional
    public void delete(Long id) {
        transacaoRepository.deleteById(id);
    }

    @Transactional
    public TransacaoDetalhadaDto buscaPoriD(Long id) {
        Transacao transacao = transacaoRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
        return modelMapper.map(transacao, TransacaoDetalhadaDto.class);
    }
}

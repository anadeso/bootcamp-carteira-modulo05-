package br.com.alura.carteira.services;

import br.com.alura.carteira.dto.UsuarioDto;
import br.com.alura.carteira.dto.UsuarioFormDto;
import br.com.alura.carteira.entities.Perfil;
import br.com.alura.carteira.entities.Usuario;
import br.com.alura.carteira.infra.RegraDeNegocioException;
import br.com.alura.carteira.repositories.PerfilRepository;
import br.com.alura.carteira.repositories.TransacaoRepository;
import br.com.alura.carteira.repositories.UsuarioRepository;
import br.com.alura.carteira.resources.TransacaoResource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Page<UsuarioDto> listar(Pageable paginacao) {
        Page<Usuario> usuarios = usuarioRepository.findAll(paginacao);
        return usuarios.map(x -> modelMapper.map(x, UsuarioDto.class));
    }

    @Transactional
    public UsuarioDto cadastrar(UsuarioFormDto usuarioFormDto) {
        Usuario usuario = modelMapper.map(usuarioFormDto, Usuario.class);

        //TODO Usando o getById pois estou associando o usuario
        //TODO Caso eu precise do carregar o perfil, todo o objeto usaria findById
        Perfil perfil = perfilRepository.getById(usuarioFormDto.getPerfilId());
        usuario.adicionarPerfil(perfil);

        String senha = new Random().nextInt(999999) + "";
        usuario.setSenha(bCryptPasswordEncoder.encode(senha));

        usuarioRepository.save(usuario);
        return modelMapper.map(usuario, UsuarioDto.class);
    }

    public void remover(Long id) {
        boolean temTransacaoCdastrada = transacaoRepository.existsByUsuarioId(id);

        if (temTransacaoCdastrada) {
            throw new RegraDeNegocioException("Usuario nao pode ser excluido pois tem transacao cadastrada");
        }
        usuarioRepository.deleteById(id);
    }
}

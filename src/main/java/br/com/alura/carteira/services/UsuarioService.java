package br.com.alura.carteira.services;

import br.com.alura.carteira.dto.UsuarioDto;
import br.com.alura.carteira.dto.UsuarioFormDto;
import br.com.alura.carteira.entities.Usuario;
import br.com.alura.carteira.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Transactional(readOnly = true)
    public Page<UsuarioDto> listar(Pageable paginacao) {
        Page<Usuario> usuarios = usuarioRepository.findAll(paginacao);
        return usuarios.map(x -> modelMapper.map(x, UsuarioDto.class));
    }

    @Transactional
    public UsuarioDto cadastrar(UsuarioFormDto usuarioFormDto) {
        Usuario usuario = modelMapper.map(usuarioFormDto, Usuario.class);

        String senha = new Random().nextInt(999999) + "";
        usuario.setSenha(senha);

        usuarioRepository.save(usuario);
        return modelMapper.map(usuario, UsuarioDto.class);
    }
}

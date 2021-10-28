package br.com.alura.carteira.repositories;

import br.com.alura.carteira.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByLogin(String login);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.perfis WHERE u.id = :idUsuario")
    Optional<Usuario> carregarPorIdComPerfis(Long idUsuario);
}

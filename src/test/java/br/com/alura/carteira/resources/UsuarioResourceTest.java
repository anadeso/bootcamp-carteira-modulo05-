package br.com.alura.carteira.resources;

import br.com.alura.carteira.entities.Perfil;
import br.com.alura.carteira.entities.Usuario;
import br.com.alura.carteira.infra.security.TokenService;
import br.com.alura.carteira.repositories.PerfilRepository;
import br.com.alura.carteira.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UsuarioResourceTest {

    // Simula requisicoes
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String token;

    @BeforeEach
    public void gerarToken() {
        // Criando user com perfil admin e salvando no banco
        Usuario logado = new Usuario("Rodrigo", "rodrigo", "123456");
        Perfil admin = perfilRepository.findById(1l).get();
        logado.adicionarPerfil(admin);
        usuarioRepository.save(logado);

        Authentication authentication = new UsernamePasswordAuthenticationToken(logado, logado.getLogin());
        this.token = tokenService.gerarToken(authentication);
    }

    @Test
    public void naoDeveriaCadastrarUsuarioComDadosIncompletos() throws Exception {
        String json = "{}";

        mvc
          .perform(MockMvcRequestBuilders
                  .post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .header("Authorization", "Bearer " +token))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void deveriaCadastrarUsuarioComDadosCompletos() throws Exception {
        String json = "{\"nome\":\"fulano\",\"login\":\"fulano@gmail.com\",\"perfilId\":1}";
        String jsonEsperado = "{\"nome\":\"fulano\",\"login\":\"fulano@gmail.com\"}";
        mvc
          .perform(MockMvcRequestBuilders
                  .post("/usuarios")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(json)
                  .header("Authorization", "Bearer " +token))
                  .andExpect(status().isCreated())
                  .andExpect(header().exists("Location"))
                  .andExpect(content().json(jsonEsperado));
    }
}

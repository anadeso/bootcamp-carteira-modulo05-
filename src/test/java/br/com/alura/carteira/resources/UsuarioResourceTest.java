package br.com.alura.carteira.resources;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

    @Test
    public void naoDeveriaCadastrarUsuarioComDadosIncompletos() throws Exception {
        String json = "{}";

        mvc
          .perform(MockMvcRequestBuilders
                  .post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void deveriaCadastrarUsuarioComDadosCompletos() throws Exception {
        String json = "{\"nome\":\"fulano\",\"login\":\"fulano@gmail.com\"}";

        mvc
          .perform(MockMvcRequestBuilders
                  .post("/usuarios")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(json))
                   .andExpect(status().isCreated())
                   .andExpect(header().exists("Location"))
                   .andExpect(content().json(json));
    }
}

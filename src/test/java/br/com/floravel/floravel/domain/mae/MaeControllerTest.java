package br.com.floravel.floravel.domain.mae;

import br.com.floravel.floravel.common.exception.GlobalExceptionHandler;
import br.com.floravel.floravel.domain.mae.dto.CadastroMaeRequestDTO;
import br.com.floravel.floravel.domain.usuario.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class MaeControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private MaeController maeController;

    @Autowired
    private MaeRepository maeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(maeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        maeRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve cadastrar uma mãe com sucesso e retornar 201 Created")
    void deveCadastrarMaeComSucesso() throws Exception {
        CadastroMaeRequestDTO request = CadastroMaeRequestDTO.builder()
                .nome("Mariana Silva")
                .email("mariana.silva@email.com")
                .senha("segredo123")
                .telefone("(11) 98765-4321")
                .cidade("São Paulo")
                .estado("SP")
                .dataNascimento(LocalDate.of(1996, 5, 20))
                .fotoUrl("/img/fotomae/fotomae.jpg")
                .build();

        mockMvc.perform(post("/api/maes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.usuarioId").isNotEmpty())
                .andExpect(jsonPath("$.nome").value("Mariana Silva"))
                .andExpect(jsonPath("$.email").value("mariana.silva@email.com"))
                .andExpect(jsonPath("$.tipoPerfil").value("MAE"))
                .andExpect(jsonPath("$.cidade").value("São Paulo"))
                .andExpect(jsonPath("$.estado").value("SP"))
                .andExpect(jsonPath("$.dataNascimento").value("1996-05-20"));
    }

    @Test
    @DisplayName("Deve retornar 409 Conflict ao tentar cadastrar mãe com e-mail duplicado")
    void deveRetornar409ParaEmailDuplicado() throws Exception {
        CadastroMaeRequestDTO request = CadastroMaeRequestDTO.builder()
                .nome("Camila Rocha")
                .email("camila.rocha@email.com")
                .senha("senha123")
                .build();

        // Primeiro cadastro
        mockMvc.perform(post("/api/maes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Segundo cadastro com mesmo e-mail
        mockMvc.perform(post("/api/maes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflito"))
                .andExpect(jsonPath("$.message", containsString("camila.rocha@email.com")));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao enviar dados inválidos")
    void deveRetornar400ParaDadosInvalidos() throws Exception {
        CadastroMaeRequestDTO requestInvalido = CadastroMaeRequestDTO.builder()
                .nome("") // Inválido: vazio
                .email("email-invalido") // Inválido: formato
                .senha("123") // Inválido: menor que 6 caracteres
                .build();

        mockMvc.perform(post("/api/maes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors.nome").exists())
                .andExpect(jsonPath("$.validationErrors.email").exists())
                .andExpect(jsonPath("$.validationErrors.senha").exists());
    }

    @Test
    @DisplayName("Deve retornar 422 Unprocessable Entity para idade biológica incompatível com maternidade")
    void deveRetornar422ParaDataNascimentoIncompativel() throws Exception {
        CadastroMaeRequestDTO requestIdadeInvalida = CadastroMaeRequestDTO.builder()
                .nome("Menina Pequena")
                .email("menina@email.com")
                .senha("senha123")
                .dataNascimento(LocalDate.now().minusYears(4)) // 4 anos de idade
                .build();

        mockMvc.perform(post("/api/maes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestIdadeInvalida)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.error").value("Regra de Negócio Violada"))
                .andExpect(jsonPath("$.message", containsString("idade incompatível")));
    }

    @Test
    @DisplayName("Deve buscar mãe por e-mail com sucesso")
    void deveBuscarMaePorEmailComSucesso() throws Exception {
        CadastroMaeRequestDTO request = CadastroMaeRequestDTO.builder()
                .nome("Beatriz Lima")
                .email("beatriz.lima@email.com")
                .senha("senha123")
                .build();

        mockMvc.perform(post("/api/maes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/maes/email/beatriz.lima@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Beatriz Lima"))
                .andExpect(jsonPath("$.email").value("beatriz.lima@email.com"));
    }
}

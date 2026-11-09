package br.com.floravel.floravel.domain.auth;

import br.com.floravel.floravel.common.exception.GlobalExceptionHandler;
import br.com.floravel.floravel.domain.auth.dto.ConfirmarEmailRequestDTO;
import br.com.floravel.floravel.domain.auth.dto.ReenviarCodigoRequestDTO;
import br.com.floravel.floravel.domain.mae.Mae;
import br.com.floravel.floravel.domain.mae.MaeRepository;
import br.com.floravel.floravel.domain.usuario.TipoPerfil;
import br.com.floravel.floravel.domain.usuario.Usuario;
import br.com.floravel.floravel.domain.usuario.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AuthController authController;

    @Autowired
    private AuthService authService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MaeRepository maeRepository;

    @Autowired
    private TokenConfirmacaoEmailRepository tokenRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        tokenRepository.deleteAll();
        maeRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve confirmar e-mail com código de 6 dígitos válido")
    void deveConfirmarEmailComCodigoValido() throws Exception {
        Usuario usuario = Usuario.builder()
                .nome("Mariana Teste")
                .email("mariana.confirmacao@email.com")
                .senha("senha123")
                .tipoPerfil(TipoPerfil.MAE)
                .emailVerificado(false)
                .build();

        Mae mae = Mae.builder()
                .usuario(usuario)
                .dataNascimento(LocalDate.of(1995, 3, 10))
                .build();
        mae = maeRepository.save(mae);
        usuario = mae.getUsuario();

        // Gera token para o usuário
        authService.gerarEEnviarCodigoConfirmacao(usuario);

        TokenConfirmacaoEmail token = tokenRepository
                .findFirstByUsuarioAndUsadoFalseOrderByDataCriacaoDesc(usuario)
                .orElseThrow();

        ConfirmarEmailRequestDTO request = ConfirmarEmailRequestDTO.builder()
                .email("mariana.confirmacao@email.com")
                .codigo(token.getCodigo())
                .build();

        mockMvc.perform(post("/api/auth/confirmar-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.verificado").value(true))
                .andExpect(jsonPath("$.email").value("mariana.confirmacao@email.com"))
                .andExpect(jsonPath("$.mensagem").exists());

        // Verifica persistência no banco
        Usuario usuarioAtualizado = usuarioRepository.findById(usuario.getId()).orElseThrow();
        assertTrue(usuarioAtualizado.getEmailVerificado());

        TokenConfirmacaoEmail tokenAtualizado = tokenRepository.findById(token.getId()).orElseThrow();
        assertTrue(tokenAtualizado.getUsado());
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao informar código incorreto")
    void deveRetornar400ParaCodigoIncorreto() throws Exception {
        Usuario usuario = Usuario.builder()
                .nome("Clara Teste")
                .email("clara@email.com")
                .senha("senha123")
                .tipoPerfil(TipoPerfil.MAE)
                .emailVerificado(false)
                .build();
        usuario = usuarioRepository.save(usuario);

        authService.gerarEEnviarCodigoConfirmacao(usuario);

        ConfirmarEmailRequestDTO requestIncorreto = ConfirmarEmailRequestDTO.builder()
                .email("clara@email.com")
                .codigo("000000") // Código errado
                .build();

        mockMvc.perform(post("/api/auth/confirmar-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestIncorreto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Código Inválido"));

        Usuario usuarioAindaNaoVerificado = usuarioRepository.findById(usuario.getId()).orElseThrow();
        assertFalse(usuarioAindaNaoVerificado.getEmailVerificado());
    }

    @Test
    @DisplayName("Deve reenviar código de 6 dígitos com sucesso")
    void deveReenviarCodigoComSucesso() throws Exception {
        Usuario usuario = Usuario.builder()
                .nome("Ana Reenvio")
                .email("ana.reenvio@email.com")
                .senha("senha123")
                .tipoPerfil(TipoPerfil.MAE)
                .emailVerificado(false)
                .build();
        usuario = usuarioRepository.save(usuario);

        authService.gerarEEnviarCodigoConfirmacao(usuario);

        ReenviarCodigoRequestDTO requestReenvio = ReenviarCodigoRequestDTO.builder()
                .email("ana.reenvio@email.com")
                .build();

        mockMvc.perform(post("/api/auth/reenviar-codigo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestReenvio)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ana.reenvio@email.com"))
                .andExpect(jsonPath("$.mensagem").exists());
    }
}

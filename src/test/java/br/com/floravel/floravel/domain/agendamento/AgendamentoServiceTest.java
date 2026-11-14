package br.com.floravel.floravel.domain.agendamento;

import br.com.floravel.floravel.common.exception.RegraDeNegocioException;
import br.com.floravel.floravel.domain.agendamento.dto.NovoAgendamentoRequestDTO;
import br.com.floravel.floravel.domain.mae.Mae;
import br.com.floravel.floravel.domain.mae.MaeRepository;
import br.com.floravel.floravel.domain.psicologo.Psicologo;
import br.com.floravel.floravel.domain.psicologo.PsicologoRepository;
import br.com.floravel.floravel.domain.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private MaeRepository maeRepository;

    @Mock
    private PsicologoRepository psicologoRepository;

    @InjectMocks
    private AgendamentoService agendamentoService;

    private Mae mae;
    private Psicologo psi;

    @BeforeEach
    void setUp() {
        Usuario userMae = Usuario.builder().id("user-1").nome("Carla Dias").build();
        mae = Mae.builder().id("mae-1").usuario(userMae).build();

        Usuario userPsi = Usuario.builder().id("user-2").nome("Dra. Paula").build();
        psi = Psicologo.builder().id("psi-1").usuario(userPsi).crp("06/98765").build();
    }

    @Test
    @DisplayName("Deve agendar consulta com duração de 50 minutos e gerar link de atendimento")
    void deveAgendarConsultaComSucesso() {
        LocalDateTime inicio = LocalDateTime.now().plusDays(2);
        NovoAgendamentoRequestDTO dto = NovoAgendamentoRequestDTO.builder()
                .maeId("mae-1")
                .psicologoId("psi-1")
                .dataHoraInicio(inicio)
                .observacoes("Primeira sessão de acolhimento")
                .build();

        when(maeRepository.findById("mae-1")).thenReturn(Optional.of(mae));
        when(psicologoRepository.findById("psi-1")).thenReturn(Optional.of(psi));

        Agendamento agendamentoSalvo = Agendamento.builder()
                .id("agend-1")
                .mae(mae)
                .psicologo(psi)
                .dataHoraInicio(inicio)
                .dataHoraFim(inicio.plusMinutes(50))
                .status("AGENDADO")
                .linkSessao("https://floravel.com.br/app/atendimento/abc-123")
                .observacoes("Primeira sessão de acolhimento")
                .build();

        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamentoSalvo);

        var response = agendamentoService.agendarConsulta(dto);

        assertNotNull(response);
        assertEquals("AGENDADO", response.getStatus());
        assertEquals("Dra. Paula", response.getPsicologoNome());
        assertEquals(inicio.plusMinutes(50), response.getDataHoraFim());
        verify(agendamentoRepository, times(1)).save(any(Agendamento.class));
    }

    @Test
    @DisplayName("Deve rejeitar cancelamento quando o horário for inferior a 2 horas de antecedência")
    void deveRejeitarCancelamentoTardio() {
        // Consulta em 30 minutos (menos de 2 horas)
        Agendamento agendamento = Agendamento.builder()
                .id("agend-1")
                .mae(mae)
                .psicologo(psi)
                .dataHoraInicio(LocalDateTime.now().plusMinutes(30))
                .dataHoraFim(LocalDateTime.now().plusMinutes(80))
                .status("AGENDADO")
                .build();

        when(agendamentoRepository.findById("agend-1")).thenReturn(Optional.of(agendamento));

        RegraDeNegocioException ex = assertThrows(RegraDeNegocioException.class,
                () -> agendamentoService.cancelarConsulta("agend-1", "mae-1"));

        assertTrue(ex.getMessage().contains("Cancelamentos só são permitidos"));
        verify(agendamentoRepository, never()).save(any(Agendamento.class));
    }
}

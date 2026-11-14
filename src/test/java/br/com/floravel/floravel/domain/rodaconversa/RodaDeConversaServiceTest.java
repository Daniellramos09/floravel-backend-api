package br.com.floravel.floravel.domain.rodaconversa;

import br.com.floravel.floravel.domain.mae.Mae;
import br.com.floravel.floravel.domain.mae.MaeRepository;
import br.com.floravel.floravel.domain.mae.exception.MaeNaoEncontradaException;
import br.com.floravel.floravel.domain.psicologo.Psicologo;
import br.com.floravel.floravel.domain.rodaconversa.dto.InscricaoRodaRequestDTO;
import br.com.floravel.floravel.domain.rodaconversa.exception.MaeJaInscritaException;
import br.com.floravel.floravel.domain.rodaconversa.exception.RodaLotadaException;
import br.com.floravel.floravel.domain.rodaconversa.exception.RodaNaoEncontradaException;
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
class RodaDeConversaServiceTest {

    @Mock
    private RodaDeConversaRepository rodaRepository;

    @Mock
    private ParticipanteRodaRepository participanteRepository;

    @Mock
    private MaeRepository maeRepository;

    @InjectMocks
    private RodaDeConversaService rodaService;

    private RodaDeConversa roda;
    private Mae mae;

    @BeforeEach
    void setUp() {
        Usuario user = Usuario.builder().id("user-1").nome("Mariana Silva").build();
        mae = Mae.builder().id("mae-1").usuario(user).build();

        Psicologo psi = Psicologo.builder().id("psi-1").usuario(Usuario.builder().nome("Dra. Helena").build()).crp("06/12345").build();

        roda = RodaDeConversa.builder()
                .id("roda-1")
                .tema("Vencendo a Depressão Pós-Parto")
                .dataHoraInicio(LocalDateTime.now().plusDays(1))
                .limiteParticipantes(10)
                .psicologo(psi)
                .status("AGENDADA")
                .build();
    }

    @Test
    @DisplayName("Deve inscrever a mãe com sucesso quando houver vagas disponíveis")
    void deveInscreverMaeComSucesso() {
        InscricaoRodaRequestDTO dto = new InscricaoRodaRequestDTO("mae-1");

        when(rodaRepository.findById("roda-1")).thenReturn(Optional.of(roda));
        when(maeRepository.findById("mae-1")).thenReturn(Optional.of(mae));
        when(participanteRepository.existsByRodaIdAndMaeIdAndStatusParticipacao("roda-1", "mae-1", "CONFIRMADA")).thenReturn(false);
        when(participanteRepository.countByRodaIdAndStatusParticipacao("roda-1", "CONFIRMADA")).thenReturn(5L);

        ParticipanteRoda participanteSalvo = ParticipanteRoda.builder()
                .id("part-1")
                .roda(roda)
                .mae(mae)
                .statusParticipacao("CONFIRMADA")
                .build();

        when(participanteRepository.findByRodaIdAndMaeId("roda-1", "mae-1")).thenReturn(Optional.empty());
        when(participanteRepository.save(any(ParticipanteRoda.class))).thenReturn(participanteSalvo);

        var response = rodaService.inscreverMae("roda-1", dto);

        assertNotNull(response);
        assertEquals("CONFIRMADA", response.getStatusParticipacao());
        assertEquals("Vencendo a Depressão Pós-Parto", response.getTemaRoda());
        verify(participanteRepository, times(1)).save(any(ParticipanteRoda.class));
    }

    @Test
    @DisplayName("Deve lançar RodaLotadaException quando o limite de participantes for atingido")
    void deveLancarExcecaoQuandoRodaLotada() {
        InscricaoRodaRequestDTO dto = new InscricaoRodaRequestDTO("mae-1");

        when(rodaRepository.findById("roda-1")).thenReturn(Optional.of(roda));
        when(maeRepository.findById("mae-1")).thenReturn(Optional.of(mae));
        when(participanteRepository.existsByRodaIdAndMaeIdAndStatusParticipacao("roda-1", "mae-1", "CONFIRMADA")).thenReturn(false);
        when(participanteRepository.countByRodaIdAndStatusParticipacao("roda-1", "CONFIRMADA")).thenReturn(10L); // Limite é 10!

        assertThrows(RodaLotadaException.class, () -> rodaService.inscreverMae("roda-1", dto));
        verify(participanteRepository, never()).save(any(ParticipanteRoda.class));
    }

    @Test
    @DisplayName("Deve lançar MaeJaInscritaException quando a mãe já estiver confirmada na roda")
    void deveLancarExcecaoQuandoMaeJaInscrita() {
        InscricaoRodaRequestDTO dto = new InscricaoRodaRequestDTO("mae-1");

        when(rodaRepository.findById("roda-1")).thenReturn(Optional.of(roda));
        when(maeRepository.findById("mae-1")).thenReturn(Optional.of(mae));
        when(participanteRepository.existsByRodaIdAndMaeIdAndStatusParticipacao("roda-1", "mae-1", "CONFIRMADA")).thenReturn(true);

        assertThrows(MaeJaInscritaException.class, () -> rodaService.inscreverMae("roda-1", dto));
        verify(participanteRepository, never()).save(any(ParticipanteRoda.class));
    }

    @Test
    @DisplayName("Deve lançar MaeNaoEncontradaException quando o ID da mãe for inexistente")
    void deveLancarExcecaoQuandoMaeNaoExistir() {
        InscricaoRodaRequestDTO dto = new InscricaoRodaRequestDTO("mae-inexistente");

        when(rodaRepository.findById("roda-1")).thenReturn(Optional.of(roda));
        when(maeRepository.findById("mae-inexistente")).thenReturn(Optional.empty());

        assertThrows(MaeNaoEncontradaException.class, () -> rodaService.inscreverMae("roda-1", dto));
    }
}

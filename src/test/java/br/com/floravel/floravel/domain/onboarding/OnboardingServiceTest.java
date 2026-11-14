package br.com.floravel.floravel.domain.onboarding;

import br.com.floravel.floravel.domain.mae.Mae;
import br.com.floravel.floravel.domain.mae.MaeRepository;
import br.com.floravel.floravel.domain.mae.exception.MaeNaoEncontradaException;
import br.com.floravel.floravel.domain.onboarding.dto.OnboardingRequestDTO;
import br.com.floravel.floravel.domain.onboarding.dto.OnboardingResponseDTO;
import br.com.floravel.floravel.domain.onboarding.service.MotorDeRecomendacao;
import br.com.floravel.floravel.domain.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OnboardingServiceTest {

    @Mock
    private OnboardingMaeRepository onboardingRepository;

    @Mock
    private MaeRepository maeRepository;

    @Mock
    private MotorDeRecomendacao motorDeRecomendacao;

    @InjectMocks
    private OnboardingService onboardingService;

    private Mae mae;
    private OnboardingRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Usuario user = Usuario.builder().id("user-1").nome("Ana Beatriz").build();
        mae = Mae.builder().id("mae-1").usuario(user).build();

        requestDTO = OnboardingRequestDTO.builder()
                .maeId("mae-1")
                .faseMaternidade("Puerpério")
                .principalDesafio("Depressão pós-parto")
                .faixaRenda("2 a 5 salários")
                .dispostoPagarPsicologia("Até R$ 50")
                .expectativaPrincipal("Acolhimento")
                .build();
    }

    @Test
    @DisplayName("Deve salvar onboarding e retornar tema calculado pelo motor de recomendação")
    void deveSalvarOnboardingComSucesso() {
        when(maeRepository.findById("mae-1")).thenReturn(Optional.of(mae));
        when(onboardingRepository.findByMaeId("mae-1")).thenReturn(Optional.empty());

        OnboardingMae onboardingSalvo = OnboardingMae.builder()
                .id("onb-1")
                .mae(mae)
                .faseMaternidade("Puerpério")
                .principalDesafio("Depressão pós-parto")
                .faixaRenda("2 a 5 salários")
                .dispostoPagarPsicologia("Até R$ 50")
                .expectativaPrincipal("Acolhimento")
                .build();

        when(onboardingRepository.save(any(OnboardingMae.class))).thenReturn(onboardingSalvo);
        when(motorDeRecomendacao.recomendar("Depressão pós-parto", "Puerpério"))
                .thenReturn(new MotorDeRecomendacao.ResultadoRecomendacao("Depressão Pós-Parto", "Acolhimento personalizado"));

        OnboardingResponseDTO response = onboardingService.salvarOuAtualizar(requestDTO);

        assertNotNull(response);
        assertEquals("Depressão Pós-Parto", response.getTemaRodaPrioritario());
        assertEquals("Ana Beatriz", response.getNomeMae());
        verify(onboardingRepository, times(1)).save(any(OnboardingMae.class));
    }

    @Test
    @DisplayName("Deve lançar MaeNaoEncontradaException ao tentar responder onboarding de mãe inexistente")
    void deveLancarExcecaoQuandoMaeInexistente() {
        when(maeRepository.findById("mae-1")).thenReturn(Optional.empty());

        assertThrows(MaeNaoEncontradaException.class, () -> onboardingService.salvarOuAtualizar(requestDTO));
        verify(onboardingRepository, never()).save(any(OnboardingMae.class));
    }
}

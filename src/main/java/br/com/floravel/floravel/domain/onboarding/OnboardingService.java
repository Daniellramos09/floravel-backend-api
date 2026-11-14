package br.com.floravel.floravel.domain.onboarding;

import br.com.floravel.floravel.domain.mae.Mae;
import br.com.floravel.floravel.domain.mae.MaeRepository;
import br.com.floravel.floravel.domain.mae.exception.MaeNaoEncontradaException;
import br.com.floravel.floravel.domain.onboarding.dto.OnboardingRequestDTO;
import br.com.floravel.floravel.domain.onboarding.dto.OnboardingResponseDTO;
import br.com.floravel.floravel.domain.onboarding.exception.OnboardingNaoEncontradoException;
import br.com.floravel.floravel.domain.onboarding.service.MotorDeRecomendacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final OnboardingMaeRepository onboardingRepository;
    private final MaeRepository maeRepository;
    private final MotorDeRecomendacao motorDeRecomendacao;

    @Transactional
    public OnboardingResponseDTO salvarOuAtualizar(OnboardingRequestDTO dto) {
        Mae mae = maeRepository.findById(dto.getMaeId())
                .orElseThrow(() -> MaeNaoEncontradaException.porId(dto.getMaeId()));

        OnboardingMae onboarding = onboardingRepository.findByMaeId(dto.getMaeId())
                .orElseGet(() -> OnboardingMae.builder().mae(mae).build());

        onboarding.setFaseMaternidade(dto.getFaseMaternidade().trim());
        onboarding.setPrincipalDesafio(dto.getPrincipalDesafio().trim());
        onboarding.setFaixaRenda(dto.getFaixaRenda().trim());
        onboarding.setDispostoPagarPsicologia(dto.getDispostoPagarPsicologia().trim());
        onboarding.setExpectativaPrincipal(dto.getExpectativaPrincipal().trim());

        OnboardingMae salvo = onboardingRepository.save(onboarding);

        // OCP & DIP: Regras de recomendação delegadas para o motor desacoplado
        MotorDeRecomendacao.ResultadoRecomendacao rec = motorDeRecomendacao.recomendar(
                salvo.getPrincipalDesafio(), salvo.getFaseMaternidade());

        return OnboardingResponseDTO.fromEntity(salvo, rec.temaPrioritario(), rec.mensagemAcolhimento());
    }

    @Transactional(readOnly = true)
    public OnboardingResponseDTO buscarPorMaeId(String maeId) {
        if (!maeRepository.existsById(maeId)) {
            throw MaeNaoEncontradaException.porId(maeId);
        }

        OnboardingMae onboarding = onboardingRepository.findByMaeId(maeId)
                .orElseThrow(() -> new OnboardingNaoEncontradoException(maeId));

        MotorDeRecomendacao.ResultadoRecomendacao rec = motorDeRecomendacao.recomendar(
                onboarding.getPrincipalDesafio(), onboarding.getFaseMaternidade());

        return OnboardingResponseDTO.fromEntity(onboarding, rec.temaPrioritario(), rec.mensagemAcolhimento());
    }
}

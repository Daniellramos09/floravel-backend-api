package br.com.floravel.floravel.domain.onboarding;

import br.com.floravel.floravel.domain.onboarding.dto.OnboardingRequestDTO;
import br.com.floravel.floravel.domain.onboarding.dto.OnboardingResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/onboarding")
@RequiredArgsConstructor
public class OnboardingController {

    private final OnboardingService onboardingService;

    @PostMapping
    public ResponseEntity<OnboardingResponseDTO> responderQuestionario(@Valid @RequestBody OnboardingRequestDTO dto) {
        OnboardingResponseDTO response = onboardingService.salvarOuAtualizar(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/mae/{maeId}")
                .buildAndExpand(response.getMaeId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/mae/{maeId}")
    public ResponseEntity<OnboardingResponseDTO> buscarPorMaeId(@PathVariable String maeId) {
        OnboardingResponseDTO response = onboardingService.buscarPorMaeId(maeId);
        return ResponseEntity.ok(response);
    }
}

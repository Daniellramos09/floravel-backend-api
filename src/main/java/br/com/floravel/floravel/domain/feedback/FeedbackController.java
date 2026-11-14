package br.com.floravel.floravel.domain.feedback;

import br.com.floravel.floravel.domain.feedback.dto.FeedbackPlataformaRequestDTO;
import br.com.floravel.floravel.domain.feedback.dto.FeedbackReuniaoRequestDTO;
import br.com.floravel.floravel.domain.feedback.dto.MuralFeedbackItemDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/reuniao")
    public ResponseEntity<Map<String, String>> registrarFeedbackReuniao(@Valid @RequestBody FeedbackReuniaoRequestDTO dto) {
        String feedbackId = feedbackService.registrarFeedbackReuniao(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(feedbackId)
                .toUri();

        return ResponseEntity.created(location).body(Map.of(
                "id", feedbackId,
                "mensagem", "Feedback da reunião registrado com sucesso!"
        ));
    }

    @PostMapping("/plataforma")
    public ResponseEntity<Map<String, String>> registrarFeedbackPlataforma(@Valid @RequestBody FeedbackPlataformaRequestDTO dto) {
        String feedbackId = feedbackService.registrarFeedbackPlataforma(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(feedbackId)
                .toUri();

        return ResponseEntity.created(location).body(Map.of(
                "id", feedbackId,
                "mensagem", "Seu depoimento foi publicado no mural com sucesso!"
        ));
    }

    @GetMapping("/mural")
    public ResponseEntity<List<MuralFeedbackItemDTO>> listarMural(
            @RequestParam(required = false, defaultValue = "Todos") String categoria
    ) {
        List<MuralFeedbackItemDTO> mural = feedbackService.listarMuralFeedbacks(categoria);
        return ResponseEntity.ok(mural);
    }
}

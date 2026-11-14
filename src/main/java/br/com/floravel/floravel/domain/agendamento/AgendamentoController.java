package br.com.floravel.floravel.domain.agendamento;

import br.com.floravel.floravel.domain.agendamento.dto.AgendamentoResponseDTO;
import br.com.floravel.floravel.domain.agendamento.dto.NovoAgendamentoRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> agendarConsulta(@Valid @RequestBody NovoAgendamentoRequestDTO dto) {
        AgendamentoResponseDTO response = agendamentoService.agendarConsulta(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/mae/{maeId}")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarPorMae(@PathVariable String maeId) {
        List<AgendamentoResponseDTO> agendamentos = agendamentoService.listarPorMae(maeId);
        return ResponseEntity.ok(agendamentos);
    }

    @DeleteMapping("/{id}/mae/{maeId}")
    public ResponseEntity<Void> cancelarConsulta(
            @PathVariable String id,
            @PathVariable String maeId
    ) {
        agendamentoService.cancelarConsulta(id, maeId);
        return ResponseEntity.noContent().build();
    }
}

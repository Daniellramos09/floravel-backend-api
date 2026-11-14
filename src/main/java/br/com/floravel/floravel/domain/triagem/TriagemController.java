package br.com.floravel.floravel.domain.triagem;

import br.com.floravel.floravel.domain.triagem.dto.CheckInTriagemRequestDTO;
import br.com.floravel.floravel.domain.triagem.dto.HistoricoTriagemResponseDTO;
import br.com.floravel.floravel.domain.triagem.dto.TriagemResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/triagens")
@RequiredArgsConstructor
public class TriagemController {

    private final TriagemService triagemService;

    @PostMapping
    public ResponseEntity<TriagemResponseDTO> registrarCheckIn(@Valid @RequestBody CheckInTriagemRequestDTO dto) {
        TriagemResponseDTO response = triagemService.registrarCheckIn(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/mae/{maeId}")
    public ResponseEntity<HistoricoTriagemResponseDTO> obterHistoricoPorMae(@PathVariable String maeId) {
        HistoricoTriagemResponseDTO historico = triagemService.obterHistoricoPorMae(maeId);
        return ResponseEntity.ok(historico);
    }
}

package br.com.floravel.floravel.domain.rodaconversa;

import br.com.floravel.floravel.domain.rodaconversa.dto.InscricaoRodaRequestDTO;
import br.com.floravel.floravel.domain.rodaconversa.dto.InscricaoRodaResponseDTO;
import br.com.floravel.floravel.domain.rodaconversa.dto.RodaResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rodas-conversa")
@RequiredArgsConstructor
public class RodaDeConversaController {

    private final RodaDeConversaService rodaService;

    @GetMapping
    public ResponseEntity<List<RodaResponseDTO>> listarRodas(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tema
    ) {
        List<RodaResponseDTO> rodas = rodaService.listarComFiltros(status, tema);
        return ResponseEntity.ok(rodas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RodaResponseDTO> buscarPorId(@PathVariable String id) {
        RodaResponseDTO roda = rodaService.buscarPorId(id);
        return ResponseEntity.ok(roda);
    }

    @PostMapping("/{id}/inscricoes")
    public ResponseEntity<InscricaoRodaResponseDTO> inscreverMae(
            @PathVariable String id,
            @Valid @RequestBody InscricaoRodaRequestDTO dto
    ) {
        InscricaoRodaResponseDTO inscricao = rodaService.inscreverMae(id, dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{inscricaoId}")
                .buildAndExpand(inscricao.getId())
                .toUri();

        return ResponseEntity.created(location).body(inscricao);
    }

    @DeleteMapping("/{id}/inscricoes/mae/{maeId}")
    public ResponseEntity<Void> cancelarInscricao(
            @PathVariable String id,
            @PathVariable String maeId
    ) {
        rodaService.cancelarInscricao(id, maeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/minhas/mae/{maeId}")
    public ResponseEntity<List<InscricaoRodaResponseDTO>> listarMinhasRodas(@PathVariable String maeId) {
        List<InscricaoRodaResponseDTO> minhasRodas = rodaService.listarMinhasRodas(maeId);
        return ResponseEntity.ok(minhasRodas);
    }
}

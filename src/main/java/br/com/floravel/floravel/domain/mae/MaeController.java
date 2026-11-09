package br.com.floravel.floravel.domain.mae;

import br.com.floravel.floravel.domain.mae.dto.AtualizacaoMaeRequestDTO;
import br.com.floravel.floravel.domain.mae.dto.CadastroMaeRequestDTO;
import br.com.floravel.floravel.domain.mae.dto.MaeResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/maes")
@RequiredArgsConstructor
public class MaeController {

    private final MaeService maeService;

    @PostMapping
    public ResponseEntity<MaeResponseDTO> cadastrarMae(@Valid @RequestBody CadastroMaeRequestDTO dto) {
        MaeResponseDTO maeCriada = maeService.cadastrarMae(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(maeCriada.getId())
                .toUri();

        return ResponseEntity.created(location).body(maeCriada);
    }

    @GetMapping
    public ResponseEntity<List<MaeResponseDTO>> listarTodas() {
        List<MaeResponseDTO> maes = maeService.listarTodas();
        return ResponseEntity.ok(maes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaeResponseDTO> buscarPorId(@PathVariable String id) {
        MaeResponseDTO mae = maeService.buscarPorId(id);
        return ResponseEntity.ok(mae);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<MaeResponseDTO> buscarPorEmail(@PathVariable String email) {
        MaeResponseDTO mae = maeService.buscarPorEmail(email);
        return ResponseEntity.ok(mae);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaeResponseDTO> atualizarMae(
            @PathVariable String id,
            @Valid @RequestBody AtualizacaoMaeRequestDTO dto
    ) {
        MaeResponseDTO maeAtualizada = maeService.atualizarMae(id, dto);
        return ResponseEntity.ok(maeAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirMae(@PathVariable String id) {
        maeService.excluirMae(id);
        return ResponseEntity.noContent().build();
    }
}

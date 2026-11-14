package br.com.floravel.floravel.domain.denuncia;

import br.com.floravel.floravel.domain.denuncia.dto.CriarDenunciaRequestDTO;
import br.com.floravel.floravel.domain.denuncia.dto.DenunciaResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/denuncias")
@RequiredArgsConstructor
public class DenunciaController {

    private final DenunciaService denunciaService;

    @PostMapping
    public ResponseEntity<DenunciaResponseDTO> registrarDenuncia(@Valid @RequestBody CriarDenunciaRequestDTO dto) {
        DenunciaResponseDTO response = denunciaService.registrarDenuncia(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<DenunciaResponseDTO>> listarPorUsuario(@PathVariable String usuarioId) {
        List<DenunciaResponseDTO> lista = denunciaService.listarPorDenunciante(usuarioId);
        return ResponseEntity.ok(lista);
    }
}

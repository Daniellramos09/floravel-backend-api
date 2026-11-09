package br.com.floravel.floravel.domain.auth;

import br.com.floravel.floravel.domain.auth.dto.ConfirmarEmailRequestDTO;
import br.com.floravel.floravel.domain.auth.dto.ConfirmarEmailResponseDTO;
import br.com.floravel.floravel.domain.auth.dto.ReenviarCodigoRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/confirmar-email")
    public ResponseEntity<ConfirmarEmailResponseDTO> confirmarEmail(
            @Valid @RequestBody ConfirmarEmailRequestDTO dto) {
        ConfirmarEmailResponseDTO resposta = authService.confirmarEmail(dto);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/reenviar-codigo")
    public ResponseEntity<ConfirmarEmailResponseDTO> reenviarCodigo(
            @Valid @RequestBody ReenviarCodigoRequestDTO dto) {
        ConfirmarEmailResponseDTO resposta = authService.reenviarCodigo(dto);
        return ResponseEntity.ok(resposta);
    }
}

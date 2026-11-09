package br.com.floravel.floravel.domain.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmarEmailResponseDTO {
    private boolean verificado;
    private String mensagem;
    private String email;
}

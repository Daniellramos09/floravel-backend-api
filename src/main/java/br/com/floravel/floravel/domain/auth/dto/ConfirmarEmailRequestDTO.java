package br.com.floravel.floravel.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmarEmailRequestDTO {

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
    private String email;

    @NotBlank(message = "O código de verificação é obrigatório.")
    @Pattern(regexp = "^\\d{6}$", message = "O código de confirmação deve conter exatamente 6 dígitos numéricos.")
    private String codigo;
}

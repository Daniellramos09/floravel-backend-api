package br.com.floravel.floravel.domain.mae.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CadastroMaeRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 150, message = "O nome não pode exceder 150 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Size(max = 150, message = "O e-mail não pode exceder 150 caracteres")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 100, message = "A senha deve conter no mínimo 6 caracteres")
    private String senha;

    @Size(max = 30, message = "O telefone não pode exceder 30 caracteres")
    private String telefone;

    @Size(max = 500, message = "A URL da foto não pode exceder 500 caracteres")
    private String fotoUrl;

    @Size(max = 100, message = "A cidade não pode exceder 100 caracteres")
    private String cidade;

    @Size(max = 50, message = "O estado não pode exceder 50 caracteres")
    private String estado;

    @Past(message = "A data de nascimento deve ser uma data válida no passado")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;
}

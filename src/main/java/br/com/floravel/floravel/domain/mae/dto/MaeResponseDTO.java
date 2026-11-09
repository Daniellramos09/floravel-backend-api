package br.com.floravel.floravel.domain.mae.dto;

import br.com.floravel.floravel.domain.mae.Mae;
import br.com.floravel.floravel.domain.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaeResponseDTO {

    private String id;
    private String usuarioId;
    private String nome;
    private String email;
    private String telefone;
    private String fotoUrl;
    private String cidade;
    private String estado;
    private String tipoPerfil;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCriacao;

    public static MaeResponseDTO fromEntity(Mae mae) {
        if (mae == null) return null;
        Usuario usuario = mae.getUsuario();

        return MaeResponseDTO.builder()
                .id(mae.getId())
                .usuarioId(usuario != null ? usuario.getId() : null)
                .nome(usuario != null ? usuario.getNome() : null)
                .email(usuario != null ? usuario.getEmail() : null)
                .telefone(usuario != null ? usuario.getTelefone() : null)
                .fotoUrl(usuario != null ? usuario.getFotoUrl() : null)
                .cidade(usuario != null ? usuario.getCidade() : null)
                .estado(usuario != null ? usuario.getEstado() : null)
                .tipoPerfil(usuario != null && usuario.getTipoPerfil() != null ? usuario.getTipoPerfil().name() : null)
                .dataNascimento(mae.getDataNascimento())
                .dataCriacao(usuario != null ? usuario.getDataCriacao() : null)
                .build();
    }
}

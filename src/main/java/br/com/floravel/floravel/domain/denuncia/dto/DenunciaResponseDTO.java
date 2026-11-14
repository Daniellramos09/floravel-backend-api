package br.com.floravel.floravel.domain.denuncia.dto;

import br.com.floravel.floravel.domain.denuncia.Denuncia;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DenunciaResponseDTO {

    private String id;
    private String denuncianteUsuarioId;
    private String denunciadoUsuarioId;
    private String salaId;
    private String motivo;
    private String oQueFez;
    private String oQueSentiu;
    private Boolean bloquearUsuario;
    private String status;
    private LocalDateTime dataDenuncia;
    private String mensagemAcolhimento;

    public static DenunciaResponseDTO fromEntity(Denuncia entity) {
        return DenunciaResponseDTO.builder()
                .id(entity.getId())
                .denuncianteUsuarioId(entity.getDenunciante().getId())
                .denunciadoUsuarioId(entity.getDenunciadoUsuarioId())
                .salaId(entity.getSalaId())
                .motivo(entity.getMotivo())
                .oQueFez(entity.getOQueFez())
                .oQueSentiu(entity.getOQueSentiu())
                .bloquearUsuario(entity.getBloquearUsuario())
                .status(entity.getStatus())
                .dataDenuncia(entity.getDataDenuncia())
                .mensagemAcolhimento("Sua denúncia foi registrada com total sigilo e acolhimento. Nossa equipe de moderação e psicologia atuará prontamente.")
                .build();
    }
}

package br.com.floravel.floravel.domain.feedback.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MuralFeedbackItemDTO {

    private String id;
    private String autor;
    private String subtitulo;
    private String categoria;
    private Integer nota;
    private String depoimento;
    private LocalDateTime data;
}

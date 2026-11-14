package br.com.floravel.floravel.domain.triagem.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoTriagemResponseDTO {
    private String maeId;
    private Integer totalTriagensRealizadas;
    private TriagemResponseDTO ultimaTriagem;
    private List<TriagemResponseDTO> historico;
}

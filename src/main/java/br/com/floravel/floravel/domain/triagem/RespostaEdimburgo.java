package br.com.floravel.floravel.domain.triagem;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "respostas_edimburgo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespostaEdimburgo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "triagem_id", nullable = false)
    private TriagemEdimburgo triagem;

    @Column(name = "numero_questao", nullable = false)
    private Integer numeroQuestao;

    @Column(name = "valor_resposta", nullable = false)
    private Integer valorResposta;
}

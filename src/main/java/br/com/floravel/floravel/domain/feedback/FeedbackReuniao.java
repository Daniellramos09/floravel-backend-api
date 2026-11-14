package br.com.floravel.floravel.domain.feedback;

import br.com.floravel.floravel.domain.reuniao.SalaReuniao;
import br.com.floravel.floravel.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks_reuniao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackReuniao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala_id", nullable = false)
    private SalaReuniao sala;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "expectativa_pre", columnDefinition = "TEXT")
    private String expectativaPre;

    @Column(name = "nota_satisfacao", nullable = false)
    private Integer notaSatisfacao;

    @Builder.Default
    @Column(name = "atendeu_expectativa", nullable = false, length = 30)
    private String atendeuExpectativa = "sim";

    @Column(name = "o_que_achou_texto", nullable = false, columnDefinition = "TEXT")
    private String oQueAchouTexto;

    @Builder.Default
    @Column(name = "permitir_divulgacao_anonima", nullable = false)
    private Boolean permitirDivulgacaoAnonima = true;

    @CreationTimestamp
    @Column(name = "data_feedback", updatable = false)
    private LocalDateTime dataFeedback;
}

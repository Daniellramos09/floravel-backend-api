package br.com.floravel.floravel.domain.rodaconversa;

import br.com.floravel.floravel.domain.mae.Mae;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "participantes_roda", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"roda_id", "mae_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipanteRoda {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "roda_id", nullable = false)
    private RodaDeConversa roda;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mae_id", nullable = false)
    private Mae mae;

    @CreationTimestamp
    @Column(name = "data_inscricao", updatable = false)
    private LocalDateTime dataInscricao;

    @Builder.Default
    @Column(name = "status_participacao", length = 50)
    private String statusParticipacao = "CONFIRMADA";
}

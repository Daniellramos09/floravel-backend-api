package br.com.floravel.floravel.domain.reuniao;

import br.com.floravel.floravel.domain.psicologo.Psicologo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "salas_reuniao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaReuniao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 50)
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "psicologo_id", nullable = false)
    private Psicologo psicologo;

    @Column(name = "referencia_id", length = 36)
    private String referenciaId;

    @Builder.Default
    @Column(nullable = false, length = 30)
    private String status = "AGENDADA";

    @Column(name = "link_sala", length = 500)
    private String linkSala;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;
}

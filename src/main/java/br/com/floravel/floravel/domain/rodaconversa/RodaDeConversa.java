package br.com.floravel.floravel.domain.rodaconversa;

import br.com.floravel.floravel.domain.psicologo.Psicologo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rodas_de_conversa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RodaDeConversa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "psicologo_id", nullable = false)
    private Psicologo psicologo;

    @Column(nullable = false, length = 200)
    private String tema;

    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    @Builder.Default
    @Column(name = "duracao_minutos", nullable = false)
    private Integer duracaoMinutos = 60;

    @Builder.Default
    @Column(name = "limite_participantes", nullable = false)
    private Integer limiteParticipantes = 15;

    @Column(name = "link_sessao", length = 500)
    private String linkSessao;

    @Builder.Default
    @Column(nullable = false, length = 50)
    private String status = "AGENDADA";
}

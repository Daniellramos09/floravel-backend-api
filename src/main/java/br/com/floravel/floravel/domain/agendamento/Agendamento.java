package br.com.floravel.floravel.domain.agendamento;

import br.com.floravel.floravel.domain.mae.Mae;
import br.com.floravel.floravel.domain.psicologo.Psicologo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mae_id", nullable = false)
    private Mae mae;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "psicologo_id", nullable = false)
    private Psicologo psicologo;

    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim", nullable = false)
    private LocalDateTime dataHoraFim;

    @Builder.Default
    @Column(length = 50)
    private String status = "AGENDADO";

    @Column(name = "link_sessao", length = 500)
    private String linkSessao;

    @Column(columnDefinition = "TEXT")
    private String observacoes;
}

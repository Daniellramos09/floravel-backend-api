package br.com.floravel.floravel.domain.onboarding;

import br.com.floravel.floravel.domain.mae.Mae;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "onboarding_mae")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnboardingMae {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mae_id", nullable = false)
    private Mae mae;

    @Column(name = "fase_maternidade", nullable = false, length = 50)
    private String faseMaternidade;

    @Column(name = "principal_desafio", nullable = false, length = 50)
    private String principalDesafio;

    @Column(name = "faixa_renda", nullable = false, length = 50)
    private String faixaRenda;

    @Column(name = "disposto_pagar_psicologia", nullable = false, length = 50)
    private String dispostoPagarPsicologia;

    @Column(name = "expectativa_principal", nullable = false, length = 50)
    private String expectativaPrincipal;

    @CreationTimestamp
    @Column(name = "data_resposta", updatable = false)
    private LocalDateTime dataResposta;
}

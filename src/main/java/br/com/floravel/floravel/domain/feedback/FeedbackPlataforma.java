package br.com.floravel.floravel.domain.feedback;

import br.com.floravel.floravel.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks_plataforma")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackPlataforma {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 50)
    private String categoria;

    @Column(nullable = false)
    private Integer nota;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensagem;

    @CreationTimestamp
    @Column(name = "data_envio", updatable = false)
    private LocalDateTime dataEnvio;
}

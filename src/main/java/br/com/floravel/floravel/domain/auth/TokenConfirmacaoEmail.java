package br.com.floravel.floravel.domain.auth;

import br.com.floravel.floravel.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens_confirmacao_email")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenConfirmacaoEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 6)
    private String codigo;

    @Column(name = "data_expiracao", nullable = false)
    private LocalDateTime dataExpiracao;

    @Builder.Default
    @Column(nullable = false)
    private Boolean usado = false;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(dataExpiracao);
    }
}

package br.com.floravel.floravel.domain.denuncia;

import br.com.floravel.floravel.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "denuncias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "denunciante_usuario_id", nullable = false)
    private Usuario denunciante;

    @Column(name = "denunciado_usuario_id", length = 36)
    private String denunciadoUsuarioId;

    @Column(name = "sala_id", length = 36)
    private String salaId;

    @Column(nullable = false, length = 50)
    private String motivo;

    @Column(name = "o_que_fez", nullable = false, columnDefinition = "TEXT")
    private String oQueFez;

    @Column(name = "o_que_sentiu", nullable = false, columnDefinition = "TEXT")
    private String oQueSentiu;

    @Builder.Default
    @Column(name = "bloquear_usuario", nullable = false)
    private Boolean bloquearUsuario = true;

    @Builder.Default
    @Column(nullable = false, length = 30)
    private String status = "PENDENTE";

    @CreationTimestamp
    @Column(name = "data_denuncia", updatable = false)
    private LocalDateTime dataDenuncia;
}

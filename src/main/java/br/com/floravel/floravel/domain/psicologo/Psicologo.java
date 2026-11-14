package br.com.floravel.floravel.domain.psicologo;

import br.com.floravel.floravel.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "psicologos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Psicologo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Column(nullable = false, unique = true, length = 50)
    private String crp;

    @Column(columnDefinition = "TEXT")
    private String biografia;

    @Column(name = "valor_consulta", precision = 10, scale = 2)
    private BigDecimal valorConsulta;
}

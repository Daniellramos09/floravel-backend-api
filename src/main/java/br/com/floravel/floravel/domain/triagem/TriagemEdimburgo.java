package br.com.floravel.floravel.domain.triagem;

import br.com.floravel.floravel.domain.mae.Mae;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "triagem_edimburgo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TriagemEdimburgo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mae_id", nullable = false)
    private Mae mae;

    @Column(name = "pontuacao_total", nullable = false)
    private Integer pontuacaoTotal;

    @Column(name = "nivel_risco", nullable = false, length = 50)
    private String nivelRisco;

    @CreationTimestamp
    @Column(name = "data_realizacao", updatable = false)
    private LocalDateTime dataRealizacao;

    @OneToMany(mappedBy = "triagem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<RespostaEdimburgo> respostas = new ArrayList<>();

    public void adicionarResposta(RespostaEdimburgo resposta) {
        respostas.add(resposta);
        resposta.setTriagem(this);
    }
}

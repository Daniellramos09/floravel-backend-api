package br.com.floravel.floravel.domain.feedback;

import br.com.floravel.floravel.common.exception.RegraDeNegocioException;
import br.com.floravel.floravel.domain.feedback.dto.FeedbackPlataformaRequestDTO;
import br.com.floravel.floravel.domain.feedback.dto.FeedbackReuniaoRequestDTO;
import br.com.floravel.floravel.domain.feedback.dto.MuralFeedbackItemDTO;
import br.com.floravel.floravel.domain.reuniao.SalaReuniao;
import br.com.floravel.floravel.domain.reuniao.SalaReuniaoRepository;
import br.com.floravel.floravel.domain.usuario.Usuario;
import br.com.floravel.floravel.domain.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackReuniaoRepository feedbackReuniaoRepository;
    private final FeedbackPlataformaRepository feedbackPlataformaRepository;
    private final SalaReuniaoRepository salaReuniaoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public String registrarFeedbackReuniao(FeedbackReuniaoRequestDTO dto) {
        SalaReuniao sala = salaReuniaoRepository.findById(dto.getSalaId())
                .orElseThrow(() -> new RegraDeNegocioException("Sala de reunião não encontrada para o ID informado."));

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado para registrar o feedback."));

        FeedbackReuniao feedback = FeedbackReuniao.builder()
                .sala(sala)
                .usuario(usuario)
                .expectativaPre(dto.getExpectativaPre())
                .notaSatisfacao(dto.getNotaSatisfacao())
                .atendeuExpectativa(dto.getAtendeuExpectativa())
                .oQueAchouTexto(dto.getOQueAchouTexto().trim())
                .permitirDivulgacaoAnonima(dto.getPermitirDivulgacaoAnonima() != null ? dto.getPermitirDivulgacaoAnonima() : true)
                .build();

        FeedbackReuniao salvo = feedbackReuniaoRepository.save(feedback);
        return salvo.getId();
    }

    @Transactional
    public String registrarFeedbackPlataforma(FeedbackPlataformaRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado para registrar o feedback."));

        FeedbackPlataforma feedback = FeedbackPlataforma.builder()
                .usuario(usuario)
                .categoria(dto.getCategoria().trim())
                .nota(dto.getNota())
                .mensagem(dto.getMensagem().trim())
                .build();

        FeedbackPlataforma salvo = feedbackPlataformaRepository.save(feedback);
        return salvo.getId();
    }

    @Transactional(readOnly = true)
    public List<MuralFeedbackItemDTO> listarMuralFeedbacks(String categoriaFiltro) {
        List<MuralFeedbackItemDTO> lista = new ArrayList<>();

        // Feedbacks da Plataforma
        List<FeedbackPlataforma> plataforma = feedbackPlataformaRepository.findAllByOrderByDataEnvioDesc();
        for (FeedbackPlataforma fp : plataforma) {
            if (categoriaFiltro != null && !categoriaFiltro.isBlank() && !categoriaFiltro.equalsIgnoreCase("Todos")) {
                if (!fp.getCategoria().equalsIgnoreCase(categoriaFiltro)) {
                    continue;
                }
            }
            String autorAnonimo = anonimizarNome(fp.getUsuario() != null ? fp.getUsuario().getNome() : "Mãe Acolhida");
            lista.add(MuralFeedbackItemDTO.builder()
                    .id(fp.getId())
                    .autor(autorAnonimo)
                    .subtitulo("Comunidade Florável")
                    .categoria(fp.getCategoria())
                    .nota(fp.getNota())
                    .depoimento(fp.getMensagem())
                    .data(fp.getDataEnvio())
                    .build());
        }

        // Feedbacks de Reunião Públicos
        List<FeedbackReuniao> reunioes = feedbackReuniaoRepository.findPublicosParaMural();
        for (FeedbackReuniao fr : reunioes) {
            String cat = "Rodas de Conversa";
            if (categoriaFiltro != null && !categoriaFiltro.isBlank() && !categoriaFiltro.equalsIgnoreCase("Todos")) {
                if (!cat.equalsIgnoreCase(categoriaFiltro)) {
                    continue;
                }
            }
            String autorAnonimo = anonimizarNome(fr.getUsuario() != null ? fr.getUsuario().getNome() : "Mãe Participante");
            String subtitulo = fr.getSala() != null ? fr.getSala().getTitulo() : "Encontro Florável";

            lista.add(MuralFeedbackItemDTO.builder()
                    .id(fr.getId())
                    .autor(autorAnonimo)
                    .subtitulo(subtitulo)
                    .categoria(cat)
                    .nota(fr.getNotaSatisfacao())
                    .depoimento(fr.getOQueAchouTexto())
                    .data(fr.getDataFeedback())
                    .build());
        }

        return lista;
    }

    private String anonimizarNome(String nomeCompleto) {
        if (nomeCompleto == null || nomeCompleto.isBlank()) return "Mãe Acolhida";
        String[] partes = nomeCompleto.trim().split("\\s+");
        if (partes.length == 1) return partes[0];
        return partes[0] + " " + partes[partes.length - 1].charAt(0) + ".";
    }
}

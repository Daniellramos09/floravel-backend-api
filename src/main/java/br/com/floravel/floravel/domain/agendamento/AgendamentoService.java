package br.com.floravel.floravel.domain.agendamento;

import br.com.floravel.floravel.common.exception.RegraDeNegocioException;
import br.com.floravel.floravel.domain.agendamento.dto.AgendamentoResponseDTO;
import br.com.floravel.floravel.domain.agendamento.dto.NovoAgendamentoRequestDTO;
import br.com.floravel.floravel.domain.mae.Mae;
import br.com.floravel.floravel.domain.mae.MaeRepository;
import br.com.floravel.floravel.domain.mae.exception.MaeNaoEncontradaException;
import br.com.floravel.floravel.domain.psicologo.Psicologo;
import br.com.floravel.floravel.domain.psicologo.PsicologoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private static final int DURACAO_CONSULTA_MINUTOS = 50;
    private static final int ANTECEDENCIA_MINIMA_CANCELAMENTO_HORAS = 2;

    private final AgendamentoRepository agendamentoRepository;
    private final MaeRepository maeRepository;
    private final PsicologoRepository psicologoRepository;

    @Transactional
    public AgendamentoResponseDTO agendarConsulta(NovoAgendamentoRequestDTO dto) {
        Mae mae = maeRepository.findById(dto.getMaeId())
                .orElseThrow(() -> MaeNaoEncontradaException.porId(dto.getMaeId()));

        Psicologo psicologo = psicologoRepository.findById(dto.getPsicologoId())
                .orElseThrow(() -> new RegraDeNegocioException("Psicóloga não encontrada para o ID informado."));

        LocalDateTime inicio = dto.getDataHoraInicio();
        LocalDateTime fim = inicio.plusMinutes(DURACAO_CONSULTA_MINUTOS);

        String linkSala = "https://floravel.com.br/app/atendimento/" + UUID.randomUUID();

        Agendamento agendamento = Agendamento.builder()
                .mae(mae)
                .psicologo(psicologo)
                .dataHoraInicio(inicio)
                .dataHoraFim(fim)
                .status("AGENDADO")
                .linkSessao(linkSala)
                .observacoes(dto.getObservacoes() != null ? dto.getObservacoes().trim() : null)
                .build();

        Agendamento salvo = agendamentoRepository.save(agendamento);
        return AgendamentoResponseDTO.fromEntity(salvo);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponseDTO> listarPorMae(String maeId) {
        if (!maeRepository.existsById(maeId)) {
            throw MaeNaoEncontradaException.porId(maeId);
        }

        return agendamentoRepository.findByMaeIdOrderByDataHoraInicioAsc(maeId).stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public void cancelarConsulta(String agendamentoId, String maeId) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new RegraDeNegocioException("Agendamento não encontrado."));

        if (!agendamento.getMae().getId().equals(maeId)) {
            throw new RegraDeNegocioException("Você não tem autorização para cancelar esta consulta.");
        }

        if (agendamento.getDataHoraInicio().isBefore(LocalDateTime.now().plusHours(ANTECEDENCIA_MINIMA_CANCELAMENTO_HORAS))) {
            throw new RegraDeNegocioException("Cancelamentos só são permitidos com pelo menos " +
                    ANTECEDENCIA_MINIMA_CANCELAMENTO_HORAS + " horas de antecedência.");
        }

        agendamento.setStatus("CANCELADO");
        agendamentoRepository.save(agendamento);
    }
}

package br.com.floravel.floravel.domain.rodaconversa;

import br.com.floravel.floravel.domain.mae.Mae;
import br.com.floravel.floravel.domain.mae.MaeRepository;
import br.com.floravel.floravel.domain.mae.exception.MaeNaoEncontradaException;
import br.com.floravel.floravel.domain.rodaconversa.dto.InscricaoRodaRequestDTO;
import br.com.floravel.floravel.domain.rodaconversa.dto.InscricaoRodaResponseDTO;
import br.com.floravel.floravel.domain.rodaconversa.dto.RodaResponseDTO;
import br.com.floravel.floravel.domain.rodaconversa.exception.MaeJaInscritaException;
import br.com.floravel.floravel.domain.rodaconversa.exception.RodaLotadaException;
import br.com.floravel.floravel.domain.rodaconversa.exception.RodaNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RodaDeConversaService {

    private final RodaDeConversaRepository rodaRepository;
    private final ParticipanteRodaRepository participanteRepository;
    private final MaeRepository maeRepository;

    @Transactional(readOnly = true)
    public List<RodaResponseDTO> listarComFiltros(String status, String tema) {
        List<RodaDeConversa> rodas = rodaRepository.buscarComFiltros(status, tema);
        return rodas.stream()
                .map(this::montarRodaResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RodaResponseDTO buscarPorId(String rodaId) {
        RodaDeConversa roda = rodaRepository.findById(rodaId)
                .orElseThrow(() -> new RodaNaoEncontradaException(rodaId));
        return montarRodaResponse(roda);
    }

    @Transactional
    public InscricaoRodaResponseDTO inscreverMae(String rodaId, InscricaoRodaRequestDTO dto) {
        RodaDeConversa roda = rodaRepository.findById(rodaId)
                .orElseThrow(() -> new RodaNaoEncontradaException(rodaId));

        Mae mae = maeRepository.findById(dto.getMaeId())
                .orElseThrow(() -> MaeNaoEncontradaException.porId(dto.getMaeId()));

        // Regra de Negócio: Não permitir inscrição duplicada
        boolean jaInscrita = participanteRepository.existsByRodaIdAndMaeIdAndStatusParticipacao(
                rodaId, dto.getMaeId(), "CONFIRMADA");
        if (jaInscrita) {
            throw new MaeJaInscritaException(dto.getMaeId(), rodaId);
        }

        // Regra de Negócio: Limite máximo de participantes por roda
        long totalInscritos = participanteRepository.countByRodaIdAndStatusParticipacao(rodaId, "CONFIRMADA");
        if (totalInscritos >= roda.getLimiteParticipantes()) {
            throw new RodaLotadaException(roda.getTema(), roda.getLimiteParticipantes());
        }

        ParticipanteRoda participante = participanteRepository.findByRodaIdAndMaeId(rodaId, dto.getMaeId())
                .orElseGet(() -> ParticipanteRoda.builder()
                        .roda(roda)
                        .mae(mae)
                        .build());

        participante.setStatusParticipacao("CONFIRMADA");
        ParticipanteRoda salvo = participanteRepository.save(participante);

        return InscricaoRodaResponseDTO.fromEntity(salvo);
    }

    @Transactional
    public void cancelarInscricao(String rodaId, String maeId) {
        ParticipanteRoda participante = participanteRepository.findByRodaIdAndMaeId(rodaId, maeId)
                .orElseThrow(() -> new RodaNaoEncontradaException(rodaId));

        participante.setStatusParticipacao("CANCELADA");
        participanteRepository.save(participante);
    }

    @Transactional(readOnly = true)
    public List<InscricaoRodaResponseDTO> listarMinhasRodas(String maeId) {
        if (!maeRepository.existsById(maeId)) {
            throw MaeNaoEncontradaException.porId(maeId);
        }

        return participanteRepository.findMinhasRodasAtivas(maeId).stream()
                .map(InscricaoRodaResponseDTO::fromEntity)
                .toList();
    }

    private RodaResponseDTO montarRodaResponse(RodaDeConversa roda) {
        long inscritos = participanteRepository.countByRodaIdAndStatusParticipacao(roda.getId(), "CONFIRMADA");
        return RodaResponseDTO.fromEntity(roda, (int) inscritos);
    }
}

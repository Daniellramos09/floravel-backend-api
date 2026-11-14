package br.com.floravel.floravel.domain.rodaconversa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipanteRodaRepository extends JpaRepository<ParticipanteRoda, String> {

    long countByRodaIdAndStatusParticipacao(String rodaId, String statusParticipacao);

    boolean existsByRodaIdAndMaeIdAndStatusParticipacao(String rodaId, String maeId, String statusParticipacao);

    Optional<ParticipanteRoda> findByRodaIdAndMaeId(String rodaId, String maeId);

    @Query("SELECT p FROM ParticipanteRoda p JOIN FETCH p.roda r JOIN FETCH r.psicologo psi " +
           "WHERE p.mae.id = :maeId AND p.statusParticipacao = 'CONFIRMADA' " +
           "ORDER BY r.dataHoraInicio ASC")
    List<ParticipanteRoda> findMinhasRodasAtivas(@Param("maeId") String maeId);
}

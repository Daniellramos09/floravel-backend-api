package br.com.floravel.floravel.domain.agendamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, String> {

    @Query("SELECT a FROM Agendamento a JOIN FETCH a.psicologo p JOIN FETCH p.usuario u " +
           "WHERE a.mae.id = :maeId ORDER BY a.dataHoraInicio ASC")
    List<Agendamento> findByMaeIdOrderByDataHoraInicioAsc(@Param("maeId") String maeId);

    @Query("SELECT a FROM Agendamento a WHERE a.mae.id = :maeId AND a.status = :status " +
           "ORDER BY a.dataHoraInicio ASC")
    List<Agendamento> findByMaeIdAndStatus(@Param("maeId") String maeId, @Param("status") String status);
}

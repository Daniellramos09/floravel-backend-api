package br.com.floravel.floravel.domain.rodaconversa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RodaDeConversaRepository extends JpaRepository<RodaDeConversa, String> {

    List<RodaDeConversa> findByStatus(String status);

    @Query("SELECT r FROM RodaDeConversa r WHERE " +
           "(:status IS NULL OR LOWER(r.status) = LOWER(:status)) AND " +
           "(:tema IS NULL OR LOWER(r.tema) LIKE LOWER(CONCAT('%', :tema, '%'))) " +
           "ORDER BY r.dataHoraInicio ASC")
    List<RodaDeConversa> buscarComFiltros(@Param("status") String status, @Param("tema") String tema);
}

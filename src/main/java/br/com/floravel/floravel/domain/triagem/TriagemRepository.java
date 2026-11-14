package br.com.floravel.floravel.domain.triagem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TriagemRepository extends JpaRepository<TriagemEdimburgo, String> {

    @Query("SELECT t FROM TriagemEdimburgo t LEFT JOIN FETCH t.respostas WHERE t.mae.id = :maeId ORDER BY t.dataRealizacao DESC")
    List<TriagemEdimburgo> findByMaeIdComRespostas(@Param("maeId") String maeId);

    Optional<TriagemEdimburgo> findFirstByMaeIdOrderByDataRealizacaoDesc(String maeId);
}

package br.com.floravel.floravel.domain.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackReuniaoRepository extends JpaRepository<FeedbackReuniao, String> {

    List<FeedbackReuniao> findBySalaId(String salaId);

    List<FeedbackReuniao> findByUsuarioId(String usuarioId);

    @Query("SELECT f FROM FeedbackReuniao f JOIN FETCH f.usuario u JOIN FETCH f.sala s " +
           "WHERE f.permitirDivulgacaoAnonima = true ORDER BY f.dataFeedback DESC")
    List<FeedbackReuniao> findPublicosParaMural();
}

package br.com.floravel.floravel.domain.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackPlataformaRepository extends JpaRepository<FeedbackPlataforma, String> {
    List<FeedbackPlataforma> findByCategoria(String categoria);
    List<FeedbackPlataforma> findAllByOrderByDataEnvioDesc();
}

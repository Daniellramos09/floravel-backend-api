package br.com.floravel.floravel.domain.reuniao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaReuniaoRepository extends JpaRepository<SalaReuniao, String> {
    List<SalaReuniao> findByStatus(String status);
}

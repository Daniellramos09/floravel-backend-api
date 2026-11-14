package br.com.floravel.floravel.domain.psicologo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PsicologoRepository extends JpaRepository<Psicologo, String> {
    Optional<Psicologo> findByCrp(String crp);
    Optional<Psicologo> findByUsuarioEmail(String email);
    boolean existsByCrp(String crp);
}

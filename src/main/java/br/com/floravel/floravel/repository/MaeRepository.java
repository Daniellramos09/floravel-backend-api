package br.com.floravel.floravel.repository;

import br.com.floravel.floravel.model.entity.Mae;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaeRepository extends JpaRepository<Mae, String> {

    Optional<Mae> findByUsuarioEmail(String email);

    Optional<Mae> findByUsuarioId(String usuarioId);

    boolean existsByUsuarioEmail(String email);
}

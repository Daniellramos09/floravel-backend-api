package br.com.floravel.floravel.domain.denuncia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, String> {
    List<Denuncia> findByDenuncianteId(String denuncianteId);
    List<Denuncia> findByStatus(String status);
}

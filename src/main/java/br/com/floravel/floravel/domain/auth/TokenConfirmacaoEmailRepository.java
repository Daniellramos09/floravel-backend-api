package br.com.floravel.floravel.domain.auth;

import br.com.floravel.floravel.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenConfirmacaoEmailRepository extends JpaRepository<TokenConfirmacaoEmail, String> {

    Optional<TokenConfirmacaoEmail> findFirstByUsuarioAndUsadoFalseOrderByDataCriacaoDesc(Usuario usuario);

    Optional<TokenConfirmacaoEmail> findFirstByUsuarioEmailAndCodigoAndUsadoFalseOrderByDataCriacaoDesc(String email, String codigo);

    @Modifying
    @Query("UPDATE TokenConfirmacaoEmail t SET t.usado = true WHERE t.usuario = :usuario AND t.usado = false")
    void invalidarTokensAnteriores(@Param("usuario") Usuario usuario);
}

package br.com.floravel.floravel.domain.auth;

import br.com.floravel.floravel.common.exception.RegraDeNegocioException;
import br.com.floravel.floravel.domain.auth.dto.ConfirmarEmailRequestDTO;
import br.com.floravel.floravel.domain.auth.dto.ConfirmarEmailResponseDTO;
import br.com.floravel.floravel.domain.auth.dto.ReenviarCodigoRequestDTO;
import br.com.floravel.floravel.domain.auth.exception.CodigoExpiradoException;
import br.com.floravel.floravel.domain.auth.exception.CodigoInvalidoException;
import br.com.floravel.floravel.domain.usuario.Usuario;
import br.com.floravel.floravel.domain.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenConfirmacaoEmailRepository tokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;

    private static final int TEMPO_EXPIRACAO_MINUTOS = 15;
    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public void gerarEEnviarCodigoConfirmacao(Usuario usuario) {
        // Invalida tokens anteriores não utilizados
        tokenRepository.invalidarTokensAnteriores(usuario);

        // Gera código numérico seguro de 6 dígitos (000000 - 999999)
        String codigo = String.format("%06d", secureRandom.nextInt(1_000_000));

        TokenConfirmacaoEmail token = TokenConfirmacaoEmail.builder()
                .usuario(usuario)
                .codigo(codigo)
                .dataExpiracao(LocalDateTime.now().plusMinutes(TEMPO_EXPIRACAO_MINUTOS))
                .usado(false)
                .build();

        tokenRepository.save(token);

        // Disparo de e-mail assíncrono
        emailService.enviarCodigoConfirmacao(usuario.getEmail(), usuario.getNome(), codigo);
    }

    @Transactional
    public ConfirmarEmailResponseDTO confirmarEmail(ConfirmarEmailRequestDTO dto) {
        String emailNormalizado = dto.getEmail().trim().toLowerCase();

        Usuario usuario = usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado para o e-mail informado."));

        if (Boolean.TRUE.equals(usuario.getEmailVerificado())) {
            return ConfirmarEmailResponseDTO.builder()
                    .verificado(true)
                    .mensagem("Este e-mail já se encontra confirmado.")
                    .email(emailNormalizado)
                    .build();
        }

        TokenConfirmacaoEmail token = tokenRepository
                .findFirstByUsuarioEmailAndCodigoAndUsadoFalseOrderByDataCriacaoDesc(emailNormalizado, dto.getCodigo().trim())
                .orElseThrow(() -> new CodigoInvalidoException("Código de confirmação incorreto ou já utilizado."));

        if (token.isExpirado()) {
            throw new CodigoExpiradoException("Este código expirou. Por favor, solicite o reenvio de um novo código.");
        }

        token.setUsado(true);
        usuario.setEmailVerificado(true);

        tokenRepository.save(token);
        usuarioRepository.save(usuario);

        log.info("E-mail {} verificado com sucesso pelo usuário ID {}", emailNormalizado, usuario.getId());

        return ConfirmarEmailResponseDTO.builder()
                .verificado(true)
                .mensagem("E-mail confirmado com sucesso! Sua conta Florável está pronta para uso.")
                .email(emailNormalizado)
                .build();
    }

    @Transactional
    public ConfirmarEmailResponseDTO reenviarCodigo(ReenviarCodigoRequestDTO dto) {
        String emailNormalizado = dto.getEmail().trim().toLowerCase();

        Usuario usuario = usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não cadastrado na plataforma."));

        if (Boolean.TRUE.equals(usuario.getEmailVerificado())) {
            return ConfirmarEmailResponseDTO.builder()
                    .verificado(true)
                    .mensagem("Seu e-mail já está validado e confirmado.")
                    .email(emailNormalizado)
                    .build();
        }

        gerarEEnviarCodigoConfirmacao(usuario);

        return ConfirmarEmailResponseDTO.builder()
                .verificado(false)
                .mensagem("Um novo código de confirmação de 6 dígitos foi enviado para o seu e-mail.")
                .email(emailNormalizado)
                .build();
    }
}

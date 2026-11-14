package br.com.floravel.floravel.common.exception;

import br.com.floravel.floravel.domain.auth.exception.CodigoExpiradoException;
import br.com.floravel.floravel.domain.auth.exception.CodigoInvalidoException;
import br.com.floravel.floravel.domain.mae.exception.EmailJaCadastradoException;
import br.com.floravel.floravel.domain.mae.exception.MaeNaoEncontradaException;
import br.com.floravel.floravel.domain.onboarding.exception.OnboardingNaoEncontradoException;
import br.com.floravel.floravel.domain.rodaconversa.exception.MaeJaInscritaException;
import br.com.floravel.floravel.domain.rodaconversa.exception.RodaLotadaException;
import br.com.floravel.floravel.domain.rodaconversa.exception.RodaNaoEncontradaException;
import br.com.floravel.floravel.domain.triagem.exception.TriagemInvalidaException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. RECURSOS NÃO ENCONTRADOS (404)
    @ExceptionHandler({
            MaeNaoEncontradaException.class,
            OnboardingNaoEncontradoException.class,
            RodaNaoEncontradaException.class
    })
    public ResponseEntity<ErrorResponseDTO> handleNaoEncontrado(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Não Encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRotaInexistente(NoResourceFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Endpoint Inexistente",
                "O endpoint requisitado '" + ex.getResourcePath() + "' não existe no servidor.", request);
    }

    // 2. CONFLITOS DE NEGÓCIO E DUPLICIDADE (409)
    @ExceptionHandler({EmailJaCadastradoException.class, MaeJaInscritaException.class})
    public ResponseEntity<ErrorResponseDTO> handleConflito(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Conflito", ex.getMessage(), request);
    }

    // 3. REGRAS DE NEGÓCIO VIOLADAS (422)
    @ExceptionHandler({RegraDeNegocioException.class, RodaLotadaException.class})
    public ResponseEntity<ErrorResponseDTO> handleRegraDeNegocio(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Regra de Negócio Violada", ex.getMessage(), request);
    }

    // 4. FALHA DE VALIDAÇÃO DOS CAMPOS DE FORMULÁRIO (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Dados Inválidos")
                .message("Falha de validação nos campos informados")
                .path(request.getRequestURI())
                .validationErrors(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 5. CÓDIGO INVÁLIDO OU EXPIRADO (400)
    @ExceptionHandler(CodigoInvalidoException.class)
    public ResponseEntity<ErrorResponseDTO> handleCodigoInvalido(CodigoInvalidoException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Código Inválido", ex.getMessage(), request);
    }

    @ExceptionHandler(CodigoExpiradoException.class)
    public ResponseEntity<ErrorResponseDTO> handleCodigoExpirado(CodigoExpiradoException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Código Expirado", ex.getMessage(), request);
    }

    @ExceptionHandler(TriagemInvalidaException.class)
    public ResponseEntity<ErrorResponseDTO> handleTriagemInvalida(TriagemInvalidaException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Triagem Inválida", ex.getMessage(), request);
    }

    // 6. JSON MALFORMADO OU TIPOS INCOMPATÍVEIS NO CORPO (400)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleJsonMalformado(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String mensagem = "O corpo da requisição (JSON) está malformado ou contém tipos de dados incompatíveis.";
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Formato de Requisição Inválido", mensagem, request);
    }

    // 7. PARÂMETROS DE URL COM TIPO INCORRETO (400)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String paramName = ex.getName();
        String valor = ex.getValue() != null ? ex.getValue().toString() : "null";
        String tipoEsperado = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "tipo específico";

        String mensagem = String.format("O parâmetro '%s' recebeu o valor '%s', mas esperava um(a) %s.",
                paramName, valor, tipoEsperado);

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Parâmetro Inválido", mensagem, request);
    }

    // 8. PARÂMETRO OBRIGATÓRIO AUSENTE NA URL (400)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> handleParametroAusente(MissingServletRequestParameterException ex, HttpServletRequest request) {
        String mensagem = "O parâmetro obrigatório '" + ex.getParameterName() + "' não foi informado na requisição.";
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Parâmetro Obrigatório Ausente", mensagem, request);
    }

    // 9. MÉTODO HTTP NÃO SUPORTADO (405)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleMetodoNaoSuportado(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String mensagem = "O método HTTP '" + ex.getMethod() + "' não é suportado para este endpoint.";
        return buildErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, "Método Não Permitido", mensagem, request);
    }

    // 10. VIOLAÇÃO DE INTEGRIDADE NO BANCO (409)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        String mensagem = "Não foi possível concluir a operação devido a um conflito de integridade referencial no banco de dados.";
        return buildErrorResponse(HttpStatus.CONFLICT, "Conflito de Integridade", mensagem, request);
    }

    // 11. ERRO GENÉRICO / NÃO TRATADO (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex, HttpServletRequest request) {
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Erro Interno")
                .message("Ocorreu um erro inesperado no servidor. Nossa equipe técnica já foi alertada.")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(HttpStatus status, String erro, String mensagem, HttpServletRequest request) {
        ErrorResponseDTO dto = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(erro)
                .message(mensagem)
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(dto);
    }
}

package br.com.floravel.floravel.domain.auth;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${floravel.mail.from:nao-responda@floravel.com.br}")
    private String remetente;

    @Async
    public void enviarCodigoConfirmacao(String destinatario, String nome, String codigo) {
        log.info("Disparando código de verificação para {}: [{}]", destinatario, codigo);

        if (mailSender == null) {
            logDevFallback(destinatario, codigo, "JavaMailSender não instanciado.");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(remetente, "Florável - Acolhimento Materno");
            helper.setTo(destinatario);
            helper.setSubject("🌸 Florável - Seu código de confirmação: " + codigo);

            String html = gerarTemplateEmailHtml(nome, codigo);
            helper.setText(html, true);

            mailSender.send(message);
            log.info("E-mail de confirmação enviado com sucesso via SMTP para: {}", destinatario);
        } catch (Exception e) {
            log.warn("Falha no envio via SMTP ({}: {}). Ativando fallback de desenvolvimento.", 
                    e.getClass().getSimpleName(), e.getMessage());
            logDevFallback(destinatario, codigo, e.getMessage());
        }
    }

    private void logDevFallback(String destinatario, String codigo, String motivo) {
        log.info("""
                
                ========================================================================================
                [FLORÁVEL EMAIL - DEV / DEMO DAY FALLBACK]
                Destinatário: {}
                Código de Confirmação: [ {} ] (Expira em 15 minutos)
                Motivo/Status SMTP: {}
                ========================================================================================
                """, destinatario, codigo, motivo);
    }

    private String gerarTemplateEmailHtml(String nome, String codigo) {
        String nomeExibicao = (nome != null && !nome.isBlank()) ? nome : "Mãe";

        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                  <meta charset="UTF-8">
                  <style>
                    body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #FAF7F5; margin: 0; padding: 24px; color: #2D3748; }
                    .container { max-width: 520px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; padding: 36px; border: 1px solid #EAE3DE; box-shadow: 0 4px 12px rgba(0,0,0,0.03); }
                    .header { text-align: center; padding-bottom: 20px; border-bottom: 1px solid #F0EAE5; }
                    .brand { font-size: 28px; font-weight: bold; color: #2D5A43; letter-spacing: -0.5px; }
                    .subtitle { font-size: 13px; color: #7A8B7B; margin-top: 4px; }
                    .content { padding: 24px 0; text-align: center; }
                    .greeting { font-size: 19px; font-weight: 600; color: #2D5A43; margin-bottom: 12px; }
                    .message { font-size: 15px; line-height: 1.6; color: #4A5568; margin-bottom: 20px; }
                    .code-box { background-color: #F4F7F4; border: 2px dashed #2D5A43; border-radius: 12px; padding: 18px; margin: 24px 0; }
                    .code { font-size: 36px; font-weight: 800; color: #2D5A43; letter-spacing: 8px; font-family: monospace; }
                    .expiration { font-size: 13px; color: #8C7872; margin-top: 8px; }
                    .footer { text-align: center; font-size: 12px; color: #A0AEC0; padding-top: 20px; border-top: 1px solid #F0EAE5; }
                  </style>
                </head>
                <body>
                  <div class="container">
                    <div class="header">
                      <div class="brand">florável</div>
                      <div class="subtitle">Cuidado & Acolhimento Materno</div>
                    </div>
                    <div class="content">
                      <div class="greeting">Olá, %s! 🌸</div>
                      <div class="message">
                        Ficamos muito felizes em ter você aqui no Florável! Para confirmar seu e-mail e ativar o seu acesso com segurança, utilize o código de 6 dígitos abaixo:
                      </div>
                      <div class="code-box">
                        <div class="code">%s</div>
                        <div class="expiration">⏱️ Este código expira em 15 minutos.</div>
                      </div>
                      <div class="message" style="font-size: 13px; color: #718096;">
                        Se você não solicitou este cadastro no Florável, por favor ignore este e-mail.
                      </div>
                    </div>
                    <div class="footer">
                      Florável © 2026 • Apoio e acolhimento para mães em todos os momentos.
                    </div>
                  </div>
                </body>
                </html>
                """.formatted(nomeExibicao, codigo);
    }
}

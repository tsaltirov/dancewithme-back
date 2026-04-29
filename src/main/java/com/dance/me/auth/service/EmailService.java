package com.dance.me.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dance.me.config.AppProperties;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final AppProperties appProperties;

    @Async
    public void sendVerificationEmail(String toEmail, String userName, String verificationUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(appProperties.getName() + " <noreply@dancewithme.com>");
            helper.setTo(toEmail);
            helper.setSubject("Confirma tu cuenta en " + appProperties.getName());
            helper.setText(buildVerificationHtml(userName, verificationUrl), true);

            mailSender.send(message);
            log.info("Verification email sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send verification email to {}: {}", toEmail, e.getMessage(), e);
        }
    }

    @Async
    public void sendPasswordResetEmail(String toEmail, String userName, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(appProperties.getName() + " <noreply@dancewithme.com>");
            helper.setTo(toEmail);
            helper.setSubject("Tu código para restablecer la contraseña");
            helper.setText(buildPasswordResetHtml(userName, code), true);

            mailSender.send(message);
            log.info("Password reset email sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}: {}", toEmail, e.getMessage(), e);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HTML email template
    //
    // Cross-client compatibility rules applied:
    //  • Table-based layout (no flexbox/grid — Outlook doesn't support them)
    //  • All CSS inline (Gmail strips <style> blocks in <head>)
    //  • bgcolor="" attribute on <td> alongside inline style (Outlook uses attribute)
    //  • width="" attribute on <table> alongside CSS (Outlook uses attribute)
    //  • Web-safe font stack (custom fonts don't load in most clients)
    //  • CTA as <a> with display:inline-block (not <button>)
    //  • MSO conditional comments to fix Outlook button padding
    //  • Max width 600px — email standard
    //  • word-break:break-all on URLs — prevents overflow in narrow clients
    // ─────────────────────────────────────────────────────────────────────────
    private String buildVerificationHtml(String userName, String verificationUrl) {
        return """
                <!DOCTYPE html>
                <html lang="es" xmlns="http://www.w3.org/1999/xhtml">
                <head>
                  <meta charset="UTF-8" />
                  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                  <title>Confirma tu cuenta</title>
                  <!--[if mso]>
                  <noscript><xml><o:OfficeDocumentSettings>
                    <o:PixelsPerInch>96</o:PixelsPerInch>
                  </o:OfficeDocumentSettings></xml></noscript>
                  <![endif]-->
                </head>
                <body style="margin:0;padding:0;background-color:#f4f4f7;
                             font-family:Arial,Helvetica,sans-serif;">

                  <!-- Outer wrapper -->
                  <table width="100%%" border="0" cellpadding="0" cellspacing="0"
                         bgcolor="#f4f4f7" style="background-color:#f4f4f7;">
                    <tr>
                      <td align="center" style="padding:40px 16px;">

                        <!-- Card 600px -->
                        <table width="600" border="0" cellpadding="0" cellspacing="0"
                               style="width:100%%;max-width:600px;">

                          <!-- ── HEADER ── -->
                          <tr>
                            <td bgcolor="#1a1a2e" align="center"
                                style="background-color:#1a1a2e;padding:40px 32px;
                                       border-radius:8px 8px 0 0;">
                              <p style="margin:0;font-size:30px;font-weight:700;
                                        color:#e94560;letter-spacing:1px;
                                        font-family:Arial,sans-serif;">
                                Dance<span style="color:#ffffff;">WithMe</span>
                              </p>
                              <p style="margin:8px 0 0;font-size:12px;color:#8888a8;
                                        letter-spacing:3px;text-transform:uppercase;
                                        font-family:Arial,sans-serif;">
                                Tu academia de baile
                              </p>
                            </td>
                          </tr>

                          <!-- ── BODY ── -->
                          <tr>
                            <td bgcolor="#ffffff" align="left"
                                style="background-color:#ffffff;padding:44px 44px 36px;">

                              <p style="margin:0 0 12px;font-size:22px;font-weight:700;
                                        color:#1a1a2e;font-family:Arial,sans-serif;">
                                ¡Bienvenido/a, %s!
                              </p>

                              <p style="margin:0 0 16px;font-size:15px;line-height:1.75;
                                        color:#555570;font-family:Arial,sans-serif;">
                                Gracias por unirte a <strong style="color:#1a1a2e;">DanceWithMe</strong>.
                                Solo falta un paso: confirmar tu dirección de correo para activar tu cuenta.
                              </p>

                              <p style="margin:0 0 36px;font-size:15px;line-height:1.75;
                                        color:#555570;font-family:Arial,sans-serif;">
                                Este enlace es válido durante <strong>24 horas</strong>.
                              </p>

                              <!-- ── CTA BUTTON ── -->
                              <table border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                  <td bgcolor="#e94560" align="center"
                                      style="background-color:#e94560;border-radius:6px;
                                             mso-padding-alt:0;">
                                    <!--[if mso]>
                                    <v:roundrect xmlns:v="urn:schemas-microsoft-com:vml"
                                      xmlns:w="urn:schemas-microsoft-com:office:word"
                                      href="%s"
                                      style="height:52px;v-text-anchor:middle;width:220px;"
                                      arcsize="8%%" stroke="f" fillcolor="#e94560">
                                      <w:anchorlock/>
                                      <center style="color:#ffffff;font-family:Arial,sans-serif;
                                                     font-size:16px;font-weight:700;">
                                        Confirmar mi cuenta
                                      </center>
                                    </v:roundrect>
                                    <![endif]-->
                                    <!--[if !mso]><!-->
                                    <a href="%s"
                                       style="display:inline-block;padding:16px 36px;
                                              color:#ffffff;font-size:16px;font-weight:700;
                                              text-decoration:none;border-radius:6px;
                                              font-family:Arial,sans-serif;">
                                      Confirmar mi cuenta
                                    </a>
                                    <!--<![endif]-->
                                  </td>
                                </tr>
                              </table>

                            </td>
                          </tr>

                          <!-- ── DIVIDER ── -->
                          <tr>
                            <td bgcolor="#ffffff" style="background-color:#ffffff;
                                padding:0 44px 32px;">
                              <table width="100%%" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                  <td style="border-top:1px solid #ebebf5;
                                             font-size:0;line-height:0;">&nbsp;</td>
                                </tr>
                              </table>
                            </td>
                          </tr>

                          <!-- ── FALLBACK URL ── -->
                          <tr>
                            <td bgcolor="#ffffff" align="left"
                                style="background-color:#ffffff;padding:0 44px 44px;">
                              <p style="margin:0 0 6px;font-size:13px;color:#9999b0;
                                        font-family:Arial,sans-serif;">
                                Si el botón no funciona, copia este enlace en tu navegador:
                              </p>
                              <p style="margin:0;font-size:12px;word-break:break-all;
                                        font-family:Arial,sans-serif;">
                                <a href="%s" style="color:#e94560;text-decoration:underline;">%s</a>
                              </p>
                            </td>
                          </tr>

                          <!-- ── FOOTER ── -->
                          <tr>
                            <td bgcolor="#f4f4f7" align="center"
                                style="background-color:#f4f4f7;padding:28px 44px;
                                       border-radius:0 0 8px 8px;">
                              <p style="margin:0 0 6px;font-size:12px;color:#9999b0;
                                        font-family:Arial,sans-serif;">
                                Si no creaste una cuenta en DanceWithMe, ignora este correo.
                              </p>
                              <p style="margin:0;font-size:12px;color:#b8b8cc;
                                        font-family:Arial,sans-serif;">
                                &copy; 2026 DanceWithMe &mdash; Todos los derechos reservados.
                              </p>
                            </td>
                          </tr>

                        </table>
                        <!-- /Card -->

                      </td>
                    </tr>
                  </table>
                  <!-- /Outer wrapper -->

                </body>
                </html>
                """.formatted(userName, verificationUrl, verificationUrl, verificationUrl, verificationUrl);
    }

    // Cada dígito del código en su propia celda — compatible con todos los clientes
    private String buildPasswordResetHtml(String userName, String code) {
        String digitCells = buildDigitCells(code);
        return """
                <!DOCTYPE html>
                <html lang="es" xmlns="http://www.w3.org/1999/xhtml">
                <head>
                  <meta charset="UTF-8" />
                  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                  <title>Restablecer contraseña</title>
                  <!--[if mso]>
                  <noscript><xml><o:OfficeDocumentSettings>
                    <o:PixelsPerInch>96</o:PixelsPerInch>
                  </o:OfficeDocumentSettings></xml></noscript>
                  <![endif]-->
                </head>
                <body style="margin:0;padding:0;background-color:#f4f4f7;
                             font-family:Arial,Helvetica,sans-serif;">

                  <table width="100%%" border="0" cellpadding="0" cellspacing="0"
                         bgcolor="#f4f4f7" style="background-color:#f4f4f7;">
                    <tr>
                      <td align="center" style="padding:40px 16px;">

                        <table width="600" border="0" cellpadding="0" cellspacing="0"
                               style="width:100%%;max-width:600px;">

                          <!-- ── HEADER ── -->
                          <tr>
                            <td bgcolor="#1a1a2e" align="center"
                                style="background-color:#1a1a2e;padding:40px 32px;
                                       border-radius:8px 8px 0 0;">
                              <p style="margin:0;font-size:30px;font-weight:700;
                                        color:#e94560;letter-spacing:1px;
                                        font-family:Arial,sans-serif;">
                                Dance<span style="color:#ffffff;">WithMe</span>
                              </p>
                              <p style="margin:8px 0 0;font-size:12px;color:#8888a8;
                                        letter-spacing:3px;text-transform:uppercase;
                                        font-family:Arial,sans-serif;">
                                Restablecer contraseña
                              </p>
                            </td>
                          </tr>

                          <!-- ── BODY ── -->
                          <tr>
                            <td bgcolor="#ffffff" align="left"
                                style="background-color:#ffffff;padding:44px 44px 36px;">

                              <p style="margin:0 0 12px;font-size:22px;font-weight:700;
                                        color:#1a1a2e;font-family:Arial,sans-serif;">
                                Hola, %s
                              </p>

                              <p style="margin:0 0 32px;font-size:15px;line-height:1.75;
                                        color:#555570;font-family:Arial,sans-serif;">
                                Recibimos una solicitud para restablecer tu contraseña.
                                Introduce este código en la aplicación. Expira en
                                <strong>15 minutos</strong>.
                              </p>

                              <!-- ── CÓDIGO 6 DÍGITOS ── -->
                              <table border="0" cellpadding="0" cellspacing="0"
                                     style="margin:0 auto 36px;">
                                <tr>
                                  %s
                                </tr>
                              </table>

                              <p style="margin:0;font-size:14px;line-height:1.7;
                                        color:#9999b0;font-family:Arial,sans-serif;">
                                Si no solicitaste este cambio, ignora este correo.
                                Tu contraseña no será modificada.
                              </p>

                            </td>
                          </tr>

                          <!-- ── FOOTER ── -->
                          <tr>
                            <td bgcolor="#f4f4f7" align="center"
                                style="background-color:#f4f4f7;padding:28px 44px;
                                       border-radius:0 0 8px 8px;">
                              <p style="margin:0;font-size:12px;color:#b8b8cc;
                                        font-family:Arial,sans-serif;">
                                &copy; 2026 DanceWithMe &mdash; Todos los derechos reservados.
                              </p>
                            </td>
                          </tr>

                        </table>
                      </td>
                    </tr>
                  </table>

                </body>
                </html>
                """.formatted(userName, digitCells);
    }

    // Genera un <td> por dígito con borde y fondo oscuro
    private String buildDigitCells(String code) {
        StringBuilder sb = new StringBuilder();
        for (char digit : code.toCharArray()) {
            sb.append("""
                    <td align="center" valign="middle" bgcolor="#1a1a2e"
                        style="background-color:#1a1a2e;width:52px;height:64px;
                               border:2px solid #e94560;border-radius:8px;
                               font-size:32px;font-weight:700;color:#ffffff;
                               font-family:Arial,sans-serif;padding:0 8px;
                               mso-padding-alt:0;">
                      %c
                    </td>
                    <td style="width:8px;font-size:0;line-height:0;">&nbsp;</td>
                    """.formatted(digit));
        }
        return sb.toString();
    }
}

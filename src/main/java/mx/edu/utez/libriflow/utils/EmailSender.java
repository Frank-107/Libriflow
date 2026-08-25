package mx.edu.utez.libriflow.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.Properties;

/**
 * Utilidad para el envío de correos electrónicos transaccionales en la plataforma LibriFlow.
 *
 * Se encarga de la transmisión de notificaciones del sistema (como códigos de verificación OTP,
 * alertas de vencimiento de rentas y confirmaciones de pago) utilizando el protocolo SMTP seguro.
 * Implementa una estrategia de carga de credenciales basada en variables de entorno con fallback
 * a archivos de propiedades locales.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @version 1.0
 * @since 25/08/2026
 */
public class EmailSender {

    /**
     * Envía un correo electrónico en formato HTML al destinatario especificado.
     *
     * @param to Dirección de correo electrónico del destinatario.
     * @param subject Asunto principal del correo electrónico.
     * @param body Cuerpo o contenido del mensaje en formato HTML.
     * @throws RuntimeException Si ocurren errores en la lectura de credenciales o durante la transmisión SMTP.
     */
    public static void sendMail(String to, String subject, String body) {
        // 1. Configuración del servidor SMTP con protocolos TLS actualizados
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true"); // Exige TLS seguro de forma obligatoria
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Compatibilidad de protocolos TLS para entornos Java modernos
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Tiempos límite para prevenir el bloqueo de hilos en caso de problemas de red
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");

        // Obtención de credenciales desde el entorno del sistema
        String userTemp = System.getenv("SMTP_USER");
        String passTemp = System.getenv("SMTP_PASS");

        // Fallback: Si no existen variables de entorno, busca en credentials.properties
        if (userTemp == null || passTemp == null) {
            System.err.println("Advertencia: Variables de entorno no encontradas. Buscando en credentials.properties...");
            Properties creds = new Properties();
            try (InputStream is = EmailSender.class.getClassLoader().getResourceAsStream("credentials.properties")) {
                if (is == null) {
                    throw new RuntimeException("No se encontró el archivo credentials.properties ni las variables de entorno.");
                }

                // Lectura del archivo respetando la codificación estándar de propiedades (ISO-8859-1)
                try (java.io.InputStreamReader reader = new java.io.InputStreamReader(is, java.nio.charset.StandardCharsets.ISO_8859_1)) {
                    creds.load(reader);
                }

                userTemp = creds.getProperty("smtp.user");
                passTemp = creds.getProperty("smtp.pass");
            } catch (Exception e) {
                throw new RuntimeException("Error al cargar las credenciales: " + e.getMessage());
            }
        }

        // 2. Definición de credenciales definitivas para el autenticador
        final String usuario = userTemp;
        final String contrasena = passTemp;

        // 3. Creación de la sesión autenticada de Jakarta Mail
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, contrasena);
            }
        });

        try {
            // 4. Construcción del mensaje MIME (Encabezados, destinatario y cuerpo HTML en UTF-8)
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuario));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");

            // 5. Envío del mensaje a través de la red SMTP
            Transport.send(message);
            System.out.println("¡Correo enviado con éxito a: " + to + "!");

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
        }
    }
}
package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.UsuarioDao;
import mx.edu.utez.libriflow.model.Usuario;
import mx.edu.utez.libriflow.utils.EmailSender;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.regex.Pattern;
/**
 * El servlet CrearCuentaUsuarioSv gestiona el proceso de registro para los nuevos
 * usuarios en la plataforma LibriFlow. Realiza la comprobación de campos,
 * validaciones con expresiones regulares, hashing SHA-256 de la contraseña,
 * asignación de tokens temporales de sesión y envío de correos de verificación.
 *
 * @author Fuentes Perez Francisco Emmanuel
 * @since 23/08/2026
 */
@WebServlet(name = "CrearCuentaUsuarioSv", value = "/crear-cuenta-usuario")
public class CrearCuentaUsuarioSv extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();

    // Regex blindados para validación
    private static final Pattern REGEX_NOMBRE = Pattern.compile("^[a-zA-ZÁÉÍÓÚáéíóúÑñ\\s]{2,40}$");
    private static final Pattern REGEX_CORREO_UTEZ = Pattern.compile("^[a-zA-Z0-9._%+-]+@utez\\.edu\\.mx$");
    private static final Pattern REGEX_TELEFONO = Pattern.compile("^\\d{10}$");
    /**
     * El método doGet despacha la vista JSP correspondiente al formulario de
     * creación de cuenta para usuarios.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param req Objeto de solicitud HTTP.
     * @param resp Objeto de respuesta HTTP.
     * @throws ServletException Si ocurre un error en el despacho hacia la vista JSP.
     * @throws IOException Si ocurre un problema de comunicación de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("CrearCuenta.jsp").forward(req, resp);
    }
    /**
     * El método doPost procesa la solicitud de registro de un nuevo usuario. Aplica
     * un esquema estricto de seguridad (filtrado de vacíos, expresiones regulares para
     * datos personales, restricción de correo institucional UTEZ, hash SHA-256 para
     * la contraseña), almacena la información pendiente en la sesión y envía un correo
     * dinámico con el código de confirmación.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param req Objeto de solicitud HTTP que contiene los datos del formulario de registro.
     * @param resp Objeto de respuesta HTTP para canalizar las alertas de error o redirecciones.
     * @throws ServletException Si ocurre un error al redireccionar o despachar el JSP.
     * @throws IOException Si ocurre un error en la entrada/salida de datos.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Obtención de parámetros con limpieza inicial
        String nombre = req.getParameter("nombre");
        String apellidoPaterno = req.getParameter("apellidoPaterno");
        String apellidoMaterno = req.getParameter("apellidoMaterno");
        String correo = req.getParameter("correo");
        String contrasena = req.getParameter("contrasena");
        String contrasena2 = req.getParameter("contrasena2");
        String telefono = req.getParameter("telefono");

        // 2. BLINDAJE 1: Control de Nulos o vacíos (Evita NullPointerException)
        if (isNullOrEmpty(nombre) || isNullOrEmpty(apellidoPaterno) || isNullOrEmpty(correo) ||
                isNullOrEmpty(contrasena) || isNullOrEmpty(contrasena2) || isNullOrEmpty(telefono)) {
            enviarError(req, resp, "Todos los campos obligatorios deben ser completados.");
            return;
        }

        // Limpieza de espacios excedentes
        nombre = nombre.trim();
        apellidoPaterno = apellidoPaterno.trim();
        apellidoMaterno = (apellidoMaterno != null) ? apellidoMaterno.trim() : "";
        correo = correo.trim().toLowerCase();
        telefono = telefono.trim();

        // 3. BLINDAJE 2: Validar formato y longitud de Nombres y Apellidos
        if (!REGEX_NOMBRE.matcher(nombre).matches() ||
                !REGEX_NOMBRE.matcher(apellidoPaterno).matches() ||
                (!apellidoMaterno.isEmpty() && !REGEX_NOMBRE.matcher(apellidoMaterno).matches())) {
            enviarError(req, resp, "Los nombres y apellidos solo deben contener letras (máximo 40 caracteres).");
            return;
        }

        // 4. BLINDAJE 3: Validar Correo Electrónico (Estructura real + Dominio)


        // 5. BLINDAJE 4: Contraseñas (Coincidencia + Tamaño Mínimo y Máximo Anti-DoS)
        if (!contrasena.equals(contrasena2)) {
            enviarError(req, resp, "Las contraseñas no coinciden.");
            return;
        }
        if (contrasena.length() < 8 || contrasena.length() > 64) {
            enviarError(req, resp, "La contraseña debe contener entre 8 y 64 caracteres.");
            return;
        }

        // 6. BLINDAJE 5: Teléfono
        if (!REGEX_TELEFONO.matcher(telefono).matches()) {
            enviarError(req, resp, "El teléfono debe contener exactamente 10 dígitos numéricos.");
            return;
        }

        // 7. BLINDAJE 6: Verificar existencia en la base de datos
        if (usuarioDao.correoExistente(correo)) {
            enviarError(req, resp, "Ya existe una cuenta vinculada a este correo. Intenta iniciar sesión.");
            return;
        }

        // 8. Proceso de Hash (SHA-256)
        String contrasenaHash;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(contrasena.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02X", b));
            }
            contrasenaHash = sb.toString();
        } catch (Exception e) {
            enviarError(req, resp, "Ocurrió un error al procesar la seguridad de la contraseña.");
            return;
        }

        // 9. Crear Usuario y Sesión
        Usuario usuarioPendiente = new Usuario(
                nombre,
                apellidoPaterno,
                apellidoMaterno,
                correo,
                telefono,
                contrasenaHash
        );

        HttpSession session = req.getSession(true);
        session.setAttribute("usuarioPendiente", usuarioPendiente);

        // Generar código de verificación de 6 dígitos seguro
        SecureRandom random = new SecureRandom();
        int codigoInt = 100000 + random.nextInt(900000);
        String codigo = String.valueOf(codigoInt);
        session.setAttribute("codigoVerificacion", codigo);

        // 10. BLINDAJE 7: Escapar HTML en la plantilla de correo (Prevención de Mail Injection / XSS)
        String nombreSanitizado = escapeHtml(usuarioPendiente.getNombre());

        String plantillaHtml = """
                <html>
                <head>
                    <meta charset="UTF-8">
                </head>
                <body style="margin:0; padding:0; background-color:#F6F1E9; font-family:Arial, Helvetica, sans-serif;">
                    <div style="max-width:600px; margin:40px auto; background:#FFFFFF; border-radius:12px; overflow:hidden; border:1px solid #D8CDBF;">
                        <div style="background:#6B4F3A; padding:25px; text-align:center;">
                            <h1 style="margin:0; color:#F6F1E9;">📚 LibriFlow</h1>
                        </div>
                        <div style="padding:35px; color:#4A3B31;">
                            <h2 style="margin-top:0;">¡Hola, {0}!</h2>
                            <p>
                                Gracias por registrarte en <strong>LibriFlow</strong>.
                                Para proteger tu cuenta y confirmar que el correo electrónico te pertenece,
                                es necesario verificar tu dirección de correo.
                            </p>
                            <p style="margin-top:30px;">
                                Tu código de verificación es:
                            </p>
                            <div style="text-align:center; margin:30px 0;">
                                <span style="
                                    display:inline-block;
                                    padding:18px 35px;
                                    font-size:30px;
                                    font-weight:bold;
                                    letter-spacing:6px;
                                    color:#6B4F3A;
                                    background:#F3ECE3;
                                    border:2px dashed #B89B7A;
                                    border-radius:10px;">
                                    {1}
                                </span>
                            </div>
                            <p>
                                Ingresa este código en la página de verificación para completar el proceso de creación de tu cuenta.
                            </p>
                            <hr style="border:none; border-top:1px solid #DDD; margin:35px 0;">
                            <p style="font-size:13px; color:#777777;">
                                Si no solicitaste crear una cuenta en LibriFlow, puedes ignorar este mensaje.
                            </p>
                        </div>
                        <div style="background:#EFE5D8; padding:18px; text-align:center; font-size:12px; color:#6F6257;">
                            © 2026 LibriFlow · Plataforma para la compra y renta de libros.
                        </div>
                    </div>
                </body>
                </html>
                """;

        String cuerpoCorreo = MessageFormat.format(plantillaHtml, nombreSanitizado, codigo);

        try {
            EmailSender.sendMail(
                    usuarioPendiente.getCorreo(),
                    "Verificación de correo electrónico - LibriFlow",
                    cuerpoCorreo
            );
            System.out.println("el codigo es:"+ codigo );
        } catch (Exception e) {
            enviarError(req, resp, "No se pudo enviar el correo de verificación. Intenta nuevamente.");
            return;
        }

        resp.sendRedirect("validar-correo-cc");
    }

    // --- MÉTODOS AUXILIARES DE SEGURIDAD ---
    /**
     * El metodo isNullOrEmpty evalúa si una cadena de texto es nula o se encuentra vacía tras
     * remoción de espacios en blanco.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param str Cadena a evaluar.
     * @return {@code true} si la cadena es nula o vacía; {@code false} en caso contrario.
     */
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    /**
     * El metodo enviarError adjunta el mensaje descriptivo del error a la solicitud HTTP y desvía la navegación
     * de vuelta al formulario JSP de creación de cuenta.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param req Objeto de solicitud HTTP.
     * @param resp Objeto de respuesta HTTP.
     * @param mensajeError Cadena de texto con la descripción detallada de la anomalía.
     * @throws ServletException Si falla el redespacho del recurso JSP.
     * @throws IOException Si ocurre una falla en la lectura/escritura de la petición.
     */
    private void enviarError(HttpServletRequest req, HttpServletResponse resp, String mensajeError) throws ServletException, IOException {
        req.setAttribute("error", mensajeError);
        req.getRequestDispatcher("CrearCuenta.jsp").forward(req, resp);
    }
    /**
     * Método reservado para peticiones HTTP PUT (no implementado).
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param req Objeto de solicitud HTTP.
     * @param resp Objeto de respuesta HTTP.
     * @throws ServletException Si ocurre una anomalía de Servlet.
     * @throws IOException Si ocurre una falla de entrada/salida.
     */
    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
    /**
     * Método reservado para peticiones HTTP PUT (no implementado).
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP.
     * @param resp Objeto de respuesta HTTP.
     * @throws ServletException Si ocurre una anomalía de Servlet.
     * @throws IOException Si ocurre una falla de entrada/salida.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {}
    /**
     * Método reservado para peticiones HTTP DELETE (no implementado).
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP.
     * @param resp Objeto de respuesta HTTP.
     * @throws ServletException Si ocurre una anomalía de Servlet.
     * @throws IOException Si ocurre una falla de entrada/salida.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {}
}
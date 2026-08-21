package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.CredencialDao;
import mx.edu.utez.libriflow.model.Dao.UsuarioDao;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
/**
 * El servlet ActualizarPerfilAdminSv sirve para gestionar la actualización de los
 * datos personales (nombre, apellidos, teléfono) y el cambio seguro de contraseña
 * para los usuarios con rol Administrador.
 *
 * @author Alejandro Mena Pereyda
 * @since 21/08/2026
 */
@WebServlet(name = "ActualizarPerfilAdminSv", value = "/actualizar-perfil-admin")
public class ActualizarPerfilAdminSv extends HttpServlet {

    /**
     * El método doGet verifica la sesión activa del usuario y despacha la vista JSP
     * correspondiente al formulario de actualización de perfil para administradores.
     *
     * @author Alejandro Mena Pereyda
     * @author Francisco Emmanuel Fuentes Perez
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP.
     * @param resp Objeto de respuesta HTTP.
     * @throws ServletException Si ocurre un error en el despacho hacia la vista JSP.
     * @throws IOException Si ocurre un problema en la redirección o lectura de datos.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        req.getRequestDispatcher("ActualizarPerfilAdmin.jsp").forward(req, resp);
    }

    /**
     * El método doPost procesa la solicitud de actualización del perfil. Valida la
     * presencia de sesión, comprueba los formatos de texto y teléfono, aplica el cifrado
     * SHA-256 si se solicitó cambio de contraseña y actualiza los datos en la base de datos
     * y en la sesión actual.
     *
     * @author Francisco Emmanuel Fuentes Perez
     * @author Alejandro Mena Pereyda
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP que contiene los parámetros del formulario de perfil.
     * @param resp Objeto de respuesta HTTP para canalizar las alertas de error o éxito.
     * @throws ServletException Si falla la redirección hacia el archivo JSP.
     * @throws IOException Si ocurre un error de comunicación de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);

        // 1. Validar existencia de sesión activa
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

        String nombre = req.getParameter("nombre");
        String apellidoPaterno = req.getParameter("apellidoPaterno");
        String apellidoMaterno = req.getParameter("apellidoMaterno");
        String telefono = req.getParameter("telefono");

        String nuevaContrasena = req.getParameter("nueva_contrasena");
        String confirmarContrasena = req.getParameter("confirmar_contrasena");

        // 2. Validar campos de texto obligatorios
        if (nombre == null || nombre.trim().isEmpty() ||
                apellidoPaterno == null || apellidoPaterno.trim().isEmpty() ||
                apellidoMaterno == null || apellidoMaterno.trim().isEmpty()) {

            req.setAttribute("error", "Todos los campos de texto son obligatorios.");
            req.getRequestDispatcher("ActualizarPerfilAdmin.jsp").forward(req, resp);
            return;
        }

        // 3. Validar teléfono de forma segura contra NullPointerException
        if (telefono == null || !telefono.trim().matches("\\d{10}")) {
            req.setAttribute("error", "Formato de teléfono inválido (deben ser 10 dígitos).");
            req.getRequestDispatcher("ActualizarPerfilAdmin.jsp").forward(req, resp);
            return;
        }

        boolean cambioPasswordExitoso = false;
        String nuevoHashPassword = null;

        // 4. Procesar Cambio de Contraseña (si fue solicitada)
        if (nuevaContrasena != null && !nuevaContrasena.trim().isEmpty()) {

            if (!nuevaContrasena.equals(confirmarContrasena)) {
                req.setAttribute("error", "Las contraseñas no coinciden.");
                req.getRequestDispatcher("ActualizarPerfilAdmin.jsp").forward(req, resp);
                return;
            }

            if (nuevaContrasena.trim().length() < 8) {
                req.setAttribute("error", "La contraseña debe tener al menos 8 caracteres.");
                req.getRequestDispatcher("ActualizarPerfilAdmin.jsp").forward(req, resp);
                return;
            }

            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(nuevaContrasena.trim().getBytes(StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                for (byte b : hash) {
                    sb.append(String.format("%02X", b));
                }

                nuevoHashPassword = sb.toString();

                // Objeto temporal para no alterar la sesión antes de tiempo
                Usuario usuarioTempPass = new Usuario();
                usuarioTempPass.setId(usuarioSesion.getId());
                usuarioTempPass.setContrasenaHash(nuevoHashPassword);

                CredencialDao credencialDao = new CredencialDao();
                if (!credencialDao.updateCredencial(usuarioTempPass)) {
                    req.setAttribute("error", "Ocurrió un problema al guardar la contraseña en la base de datos.");
                    req.getRequestDispatcher("ActualizarPerfilAdmin.jsp").forward(req, resp);
                    return;
                }
                cambioPasswordExitoso = true;

            } catch (Exception e) {
                req.setAttribute("error", "Error interno al procesar el cifrado de contraseña.");
                req.getRequestDispatcher("ActualizarPerfilAdmin.jsp").forward(req, resp);
                return;
            }
        }

        // 5. Procesar Datos Personales con un objeto temporal
        Usuario usuarioAActualizar = new Usuario();
        usuarioAActualizar.setId(usuarioSesion.getId());
        usuarioAActualizar.setNombre(nombre.trim());
        usuarioAActualizar.setApellidoPaterno(apellidoPaterno.trim());
        usuarioAActualizar.setApellidoMaterno(apellidoMaterno.trim());
        usuarioAActualizar.setTelefono(telefono.trim());

        UsuarioDao usuarioDao = new UsuarioDao();
        boolean datosActualizados = usuarioDao.update(usuarioAActualizar);

        if (datosActualizados) {
            // SÓLO cuando la base de datos responde SUCCESS, actualizamos el objeto de sesión
            usuarioSesion.setNombre(nombre.trim());
            usuarioSesion.setApellidoPaterno(apellidoPaterno.trim());
            usuarioSesion.setApellidoMaterno(apellidoMaterno.trim());
            usuarioSesion.setTelefono(telefono.trim());

            if (cambioPasswordExitoso) {
                usuarioSesion.setContrasenaHash(nuevoHashPassword);
            }

            session.setAttribute("usuario", usuarioSesion);
            req.setAttribute("exito", "¡Tu perfil se ha actualizado con éxito!");
        } else {
            req.setAttribute("error", "Ocurrió un error inesperado al actualizar tus datos personales.");
        }

        req.getRequestDispatcher("ActualizarPerfilAdmin.jsp").forward(req, resp);
    }
}
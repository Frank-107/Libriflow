package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.CredencialDao;
import mx.edu.utez.libriflow.model.Dao.RolDao;
import mx.edu.utez.libriflow.model.Dao.UsuarioDao;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebServlet(name = "IniciarSesionSv", value = "/iniciar-sesion")
public class IniciarSesionSv extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final CredencialDao credencialDao = new CredencialDao();
    private final RolDao rolDao = new RolDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("text/html; charset=UTF-8");

        req.getRequestDispatcher("IniciarSesion.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1. Configuración de Codificación UTF-8
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("text/html; charset=UTF-8");

        // 2. Obtención y Sanitización de Parámetros
        String correo = req.getParameter("correo");
        String contrasena = req.getParameter("contrasena");

        if (correo != null) {
            correo = correo.trim();
        }

        // 3. Validaciones de Campos Vacíos/Nulos
        if (correo == null || correo.isEmpty() || contrasena == null || contrasena.isEmpty()) {
            req.setAttribute("error", "Por favor, completa todos los campos.");
            // Si el correo no es gigantesco, lo devolvemos para comodidad del usuario
            if (correo != null && correo.length() <= 100) {
                req.setAttribute("correo", correo);
            }
            req.getRequestDispatcher("IniciarSesion.jsp").forward(req, resp);
            return;
        }

        // 4. Validación de Tamaño Máximo (Evita desbordamientos y cadenas maliciosas)
        if (correo.length() > 100 || contrasena.length() > 100) {
            req.setAttribute("error", "Los campos no pueden exceder los 100 caracteres.");
            // IMPORTANTE: NO se asigna req.setAttribute("correo", correo) para evitar romper el input/diseño
            req.setAttribute("correo", "eres un pillin eh");
            req.getRequestDispatcher("IniciarSesion.jsp").forward(req, resp);
            return;
        }

        // 5. Verificación de Usuario y Credenciales
        int idUsuario = usuarioDao.getIdUsuario(correo);

        if (idUsuario != -1 && credencialDao.validarContrasena(idUsuario, contrasena)) {
            Usuario usuario = usuarioDao.obtenerUsuario(correo);

            if (usuario == null) {
                req.setAttribute("error", "Ocurrió un error al cargar la información del usuario.");
                req.setAttribute("correo", correo);
                req.getRequestDispatcher("IniciarSesion.jsp").forward(req, resp);
                return;
            }

            // 6. Validación de Estado de la Cuenta y Autodesbloqueo
            if (!"ACTIVA".equalsIgnoreCase(usuario.getEstado())) {
                Timestamp ahora = Timestamp.valueOf(LocalDateTime.now());

                if (usuario.getFechaDesbloqueo() != null && ahora.after(usuario.getFechaDesbloqueo())) {
                    usuarioDao.activarUsuario(idUsuario);
                    usuario.setEstado("ACTIVA");
                } else {
                    req.setAttribute("error", "La cuenta está inactiva. Contacta al administrador.");
                    req.setAttribute("correo", correo);
                    req.getRequestDispatcher("IniciarSesion.jsp").forward(req, resp);
                    return;
                }
            }

            // 7. Prevención de Fijación de Sesión (Invalidar sesión anterior)
            HttpSession viejaSesion = req.getSession(false);
            if (viejaSesion != null) {
                viejaSesion.invalidate();
            }

            HttpSession session = req.getSession(true);
            String rol = rolDao.obtenerRol(idUsuario);

            session.setAttribute("usuario", usuario);
            session.setAttribute("tipo_usuario", rol);

            // 8. Redirección
            if ("ADMIN".equalsIgnoreCase(rol)) {
                resp.sendRedirect("inicio-admin");
            } else {
                resp.sendRedirect("inicio");
            }
            return;
        }

        // 9. Error en credenciales
        req.setAttribute("error", "Usuario o contraseña incorrectos. Inténtalo de nuevo.");
        req.setAttribute("correo", correo); // Correo seguro devuelto a la vista
        req.getRequestDispatcher("IniciarSesion.jsp").forward(req, resp);
    }
}
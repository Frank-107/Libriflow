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
import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebServlet(name = "IniciarSesionSv", value = "/iniciar-sesion")
public class IniciarSesionSv extends HttpServlet {

    UsuarioDao usuarioDao = new UsuarioDao();
    CredencialDao credencialDao = new CredencialDao();
    RolDao rolDao = new RolDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("IniciarSesion.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String correo = req.getParameter("correo");
        String contrasena = req.getParameter("contrasena");

        int idUsuario = usuarioDao.getIdUsuario(correo);


        boolean contrasenaCorrecta = credencialDao.validarContrasena(idUsuario, contrasena);

        if (idUsuario != -1 && contrasenaCorrecta) {
            Usuario usuario = usuarioDao.obtenerUsuario(correo);
            if (!usuario.getEstado().equals("ACTIVA")) {

                Timestamp ahora = Timestamp.valueOf(LocalDateTime.now());

                if (usuario.getFechaDesbloqueo() != null &&
                        ahora.after(usuario.getFechaDesbloqueo())) {
                    usuarioDao.activarUsuario(idUsuario);
                    usuario.setEstado("ACTIVA");
                    System.out.println(
                            "Usuario " + usuario.getCorreo() + " activado automáticamente."
                    );

                } else {

                    req.setAttribute(
                            "error",
                            "La cuenta está inactiva. Por favor, contacta al administrador."
                    );

                    req.getRequestDispatcher("IniciarSesion.jsp").forward(req, resp);
                    return;
                }
            }

            HttpSession session = req.getSession(true);


            // Obtener el rol desde la BD
            String rol = rolDao.obtenerRol(idUsuario);

            session.setAttribute("usuario", usuario);
            session.setAttribute("tipo_usuario", rol);

            if ("ADMIN".equalsIgnoreCase(rol)) {

                resp.sendRedirect("inicio-admin");

            } else {

                resp.sendRedirect("inicio");

            }

            return;
        }

        req.setAttribute("error", "Usuario o contraseña incorrectos. Inténtalo de nuevo.");
        req.getRequestDispatcher("IniciarSesion.jsp").forward(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    }
}

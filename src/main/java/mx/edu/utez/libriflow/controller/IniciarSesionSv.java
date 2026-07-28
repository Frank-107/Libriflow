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

            HttpSession session = req.getSession(true);

            Usuario usuario = usuarioDao.obtenerUsuario(correo);

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

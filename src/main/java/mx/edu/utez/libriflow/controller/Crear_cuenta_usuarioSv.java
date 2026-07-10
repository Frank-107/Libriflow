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

@WebServlet(name = "Crear_cuenta_usuarioSv", value = "/Crear_cuenta_usuarioSv")
public class Crear_cuenta_usuarioSv extends HttpServlet {
UsuarioDao usuarioDao = new UsuarioDao();
CredencialDao credencialDao = new CredencialDao();
RolDao rolDao = new RolDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String nombre = req.getParameter("nombre");
        String apellidoPaterno = req.getParameter("apellidoPaterno");
        String apellidoMaterno = req.getParameter("apellidoMaterno");
        String correo = req.getParameter("correo");
        String correo2 = req.getParameter("correo2");
        String contrasena = req.getParameter("contrasena");
        String contrasena2 = req.getParameter("contrasena2");
        String telefono = req.getParameter("telefono");

        if (!correo.endsWith("@utez.edu.mx")) {
            req.setAttribute("error", "Solo se admiten correos institucionales (UTEZ).");
            req.getRequestDispatcher("Crear_cuenta_usuario.jsp").forward(req, resp);
            return;
        }

        if (!correo.equals(correo2)) {
            req.setAttribute("error", "Los correos no coinciden.");
            req.getRequestDispatcher("Crear_cuenta_usuario.jsp").forward(req, resp);
            return;
        }

        if (!contrasena.equals(contrasena2)) {
            req.setAttribute("error", "Las contraseñas no coinciden.");
            req.getRequestDispatcher("Crear_cuenta_usuario.jsp").forward(req, resp);
            return;
        }

        if (!telefono.matches("\\d{10}")) {
            req.setAttribute("error", "Formato de teléfono inválido.");
            req.getRequestDispatcher("Crear_cuenta_usuario.jsp").forward(req, resp);
            return;
        }

        Usuario usuarioNuevo = new Usuario(
                nombre,
                apellidoPaterno,
                apellidoMaterno,
                correo,
                telefono
        );

        int idUsuarioNuevo = usuarioDao.create(usuarioNuevo);

        if (idUsuarioNuevo == -1) {
            req.setAttribute("error", "Error al crear la cuenta.");
            req.getRequestDispatcher("Crear_cuenta_usuario.jsp").forward(req, resp);
            return;
        }

        try {
            if (!credencialDao.create(contrasena, idUsuarioNuevo)) {
                throw new IllegalArgumentException("No se guardaron las credenciales.");
            }

            if (!rolDao.create(idUsuarioNuevo)) {
                throw new IllegalArgumentException("No se guardaron los roles.");
            }
            HttpSession session = req.getSession();
            session.setAttribute("mensaje", "Cuenta creada con éxito, ahora inicia sesión.");
            resp.sendRedirect("Crear_cuenta_usuarioSv");


        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("Crear_cuenta_usuario.jsp").forward(req, resp);
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}

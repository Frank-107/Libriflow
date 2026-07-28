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

@WebServlet(name = "ActualizarPerfilAdminSv", value = "/actualizar-perfil-admin")
public class ActualizarPerfilAdminSv extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("ActualizarPerfilAdmin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

        String nombre = req.getParameter("nombre");
        String apellidoPaterno = req.getParameter("apellidoPaterno");
        String apellidoMaterno = req.getParameter("apellidoMaterno");
        String telefono = req.getParameter("telefono");

        String nuevaContrasena = req.getParameter("nueva_contrasena");
        String confirmarContrasena = req.getParameter("confirmar_contrasena");


        if (!telefono.matches("\\d{10}")) {
            req.setAttribute("error", "Formato de teléfono inválido.");
            req.getRequestDispatcher("ActualizarPerfilAdmin.jsp").forward(req, resp);
            return;
        }

        if (nuevaContrasena != null && !nuevaContrasena.trim().isEmpty()) {

            if (!nuevaContrasena.equals(confirmarContrasena)) {
                req.setAttribute("error", "Las contraseñas no coinciden.");
                req.getRequestDispatcher("ActualizarPerfilAdmin.jsp").forward(req, resp);
                return;
            }
            if (nuevaContrasena.length() < 8) {
                req.setAttribute("error", "La contraseña debe tener al menos 8 caracteres.");
                req.getRequestDispatcher("ActualizarPerfilAdmin.jsp").forward(req, resp);
                return;
            }

            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(nuevaContrasena.getBytes(StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                for (byte b : hash) {
                    sb.append(String.format("%02X", b));
                }

                usuarioSesion.setContrasenaHash(sb.toString());

                CredencialDao credencialDao = new CredencialDao();
                boolean passActualizada = credencialDao.updateCredencial(usuarioSesion);

                if (!passActualizada) {
                    req.setAttribute("error", "Ocurrió un problema al guardar tu nueva contraseña. Intentalo más tarde.");
                    req.getRequestDispatcher("ActualizarPerfilAdmin.jsp").forward(req, resp);
                    return;
                }

            } catch (Exception e) {
                req.setAttribute("error", "Error al actualizar perfil.");
                req.getRequestDispatcher("ActualizarPerfilAdmin.jsp").forward(req, resp);
                return;
            }
        }

        usuarioSesion.setNombre(nombre);
        usuarioSesion.setApellidoPaterno(apellidoPaterno);
        usuarioSesion.setApellidoMaterno(apellidoMaterno);
        usuarioSesion.setTelefono(telefono);

        UsuarioDao usuarioDao = new UsuarioDao();
        boolean datosActualizados = usuarioDao.update(usuarioSesion);

        if (datosActualizados) {
            session.setAttribute("usuario", usuarioSesion);
            req.setAttribute("exito", "¡Tu perfil se ha actualizado con éxito!");
        } else {
            req.setAttribute("error", "Ocurrió un error inesperado al actualizar tus datos personales.");
        }
        req.getRequestDispatcher("ActualizarPerfilAdmin.jsp").forward(req, resp);
    }
}

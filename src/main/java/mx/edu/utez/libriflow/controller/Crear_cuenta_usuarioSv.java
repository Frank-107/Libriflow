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
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@WebServlet(name = "Crear_cuenta_usuarioSv", value = "/Crear_cuenta_usuarioSv")
public class Crear_cuenta_usuarioSv extends HttpServlet {
UsuarioDao usuarioDao = new UsuarioDao();
CredencialDao credencialDao = new CredencialDao();
RolDao rolDao = new RolDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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

            String contrasenaHash = null;
        //proceso de hash
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hash = md.digest(contrasena.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();

            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            contrasenaHash = sb.toString();
        }catch (Exception e) {
            req.setAttribute("error", "Error al procesar la contraseña.");
            req.getRequestDispatcher("Crear_cuenta_usuario.jsp").forward(req, resp);
            return;
        }

        Usuario usuarioPendiente = new Usuario(
                nombre,
                apellidoPaterno,
                apellidoMaterno,
                correo,
                telefono,
                contrasenaHash
        );
            HttpSession session = req.getSession(true);
            req.setAttribute("usuarioPendiente", usuarioPendiente);
            req.getRequestDispatcher("Validar_Correo_CCSV").forward(req, resp);

// mandarlo a la base de datos
//        int idUsuarioNuevo = usuarioDao.create(usuarioNuevo);
//
//        if (idUsuarioNuevo == -1) {
//            req.setAttribute("error", "Error al crear la cuenta.");
//            req.getRequestDispatcher("Crear_cuenta_usuario.jsp").forward(req, resp);
//            return;
//        }
//
//        try {
//            if (!credencialDao.create(contrasena, idUsuarioNuevo)) {
//                throw new IllegalArgumentException("No se guardaron las credenciales.");
//            }
//
//            if (!rolDao.create(idUsuarioNuevo)) {
//                throw new IllegalArgumentException("No se guardaron los roles.");
//            }
//
//            req.setAttribute("mensaje", "Cuenta creada con éxito, ahora inicia sesión.");
//            req.getRequestDispatcher("index.jsp").forward(req, resp);
//
//        } catch (Exception e) {
//            req.setAttribute("error", e.getMessage());
//            req.getRequestDispatcher("Crear_cuenta_usuario.jsp").forward(req, resp);
//        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}

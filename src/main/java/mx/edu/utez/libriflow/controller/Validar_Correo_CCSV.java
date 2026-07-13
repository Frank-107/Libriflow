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
@WebServlet(name = "Validar_Correo_CCSV", value = "/Validar_correo_CCSV")
public class Validar_Correo_CCSV extends HttpServlet {
    UsuarioDao usuarioDao = new UsuarioDao();
    CredencialDao credencialDao = new CredencialDao();
    RolDao rolDao = new RolDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("Validar_correo_CC.jsp");
        }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codigo = req.getParameter("codigo").trim();
        HttpSession session = req.getSession(false);

        if (session == null ||
                session.getAttribute("usuarioPendiente") == null ||
                session.getAttribute("codigoVerificacion") == null) {

            HttpSession nuevaSesion = req.getSession(true);
            nuevaSesion.setAttribute("mensaje",
                    "Tu sesión expiró. Por favor, vuelve a iniciar el proceso de registro.");

            resp.sendRedirect("index.jsp");
            return;
        }
        if(!codigo.equals(session.getAttribute("codigoVerificacion"))) {
            req.setAttribute("error", "Código de verificación incorrecto.");
            req.getRequestDispatcher("Validar_correo_CC.jsp").forward(req, resp);
        } else {
            Usuario usuarioValidado = (Usuario) req.getSession(false).getAttribute("usuarioPendiente");
            // mandarlo a la base de datos

            try {
                int idUsuarioNuevo = usuarioDao.create(usuarioValidado);

                if (idUsuarioNuevo == -1) {
                    throw new IllegalArgumentException("No se pudo crear el usuario.");
                }
                if (!credencialDao.create(usuarioValidado.getContrasenaHash(), idUsuarioNuevo)) {
                throw new IllegalArgumentException("No se guardaron las credenciales.");
            }

            if (!rolDao.create(idUsuarioNuevo)) {
                throw new IllegalArgumentException("No se guardaron los roles.");
            }

            session.setAttribute("mensaje", "Cuenta creada con éxito, ahora inicia sesión.");
            resp.sendRedirect("indexSV");

        } catch (Exception e) {
            System.err.println( e.getMessage());
            session.setAttribute("error", e.getMessage());
            resp.sendRedirect("indexSV");
          return;
        }
        }
    }
}

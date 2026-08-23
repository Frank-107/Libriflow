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

/**
 * Controlador Servlet encargado de gestionar la validación del código de verificación enviado por correo
 * durante el registro de un nuevo usuario, completando la creación de la cuenta, credenciales y roles en la base de datos.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @since 22/08/2026
 */
@WebServlet(name = "ValidarCorreoCCSv", value = "/validar-correo-cc")
public class ValidarCorreoCCSv extends HttpServlet {

    UsuarioDao usuarioDao = new UsuarioDao();
    CredencialDao credencialDao = new CredencialDao();
    RolDao rolDao = new RolDao();

    /**
     * Procesa las peticiones GET para mostrar el formulario de validación de código de correo.
     * Verifica que exista un proceso de registro pendiente registrado en la sesión.
     *
     * @param req Objeto HttpServletRequest que contiene la sesión activa del usuario.
     * @param resp Objeto HttpServletResponse para redireccionar o despachar a la vista JSP.
     * @throws ServletException Si ocurre un error durante el reenvío de la solicitud al JSP.
     * @throws IOException Si ocurre un error de entrada/salida durante el flujo HTTP.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null ||
                session.getAttribute("usuarioPendiente") == null){
            resp.sendRedirect("index");
            return;
        }
        req.getRequestDispatcher("ValidarCorreoCC.jsp").forward(req, resp);
    }

    /**
     * Procesa las peticiones POST para validar el código numérico ingresado por el usuario.
     * Si coincide con el enviado a la sesión, persiste al usuario, sus credenciales
     * y su rol predeterminado dentro del sistema.
     *
     * @param req Objeto HttpServletRequest con el parámetro del código de verificación.
     * @param resp Objeto HttpServletResponse para gestionar las redirecciones tras el proceso.
     * @throws ServletException Si ocurre un error en el procesamiento del Servlet.
     * @throws IOException Si ocurre un error de lectura/escritura durante el flujo HTTP.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
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

            resp.sendRedirect("indexSv");
            return;
        }
        if(!codigo.equals(session.getAttribute("codigoVerificacion"))) {
            req.setAttribute("error", "Código de verificación incorrecto.");
            req.getRequestDispatcher("ValidarCorreoCC.jsp").forward(req, resp);
            return;
        } else {
            Usuario usuarioValidado = (Usuario) session.getAttribute("usuarioPendiente");

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

                session.removeAttribute("usuarioPendiente");
                session.removeAttribute("codigoVerificacion");

                session.setAttribute("mensaje", "Cuenta creada con éxito, ahora inicia sesión.");
                resp.sendRedirect("index");

            } catch (Exception e) {
                System.err.println(e.getMessage());
                session.setAttribute("error", e.getMessage());
                resp.sendRedirect("indexSv");
                return;
            }
        }
    }
}
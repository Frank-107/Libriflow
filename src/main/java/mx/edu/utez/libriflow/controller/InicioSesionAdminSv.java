package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controlador Servlet encargado de validar el acceso seguro a las vistas del administrador.
 * Comprueba la existencia de una sesión activa y verifica que el tipo de usuario posea
 * el rol de administrador antes de permitir el reenvío hacia la página correspondiente.
 *
 * @author Monserrath Anzurez
 * @since 23/08/26
 */
@WebServlet(name = "InicioSesionAdminSv", value = "/inicio-sesion-admin")
public class InicioSesionAdminSv extends HttpServlet {

    /**
     * Procesa las peticiones GET para verificar las credenciales de sesión y el rol del usuario.
     * Si la sesión es nula o el rol no corresponde a 'ADMIN', redirige al usuario hacia el flujo de inicio
     * de sesión o la página principal; de lo contrario, despacha la petición al panel administrativo.
     *
     * @param req Objeto HttpServletRequest que transporta la sesión del usuario.
     * @param resp Objeto HttpServletResponse para realizar las redirecciones o el reenvío de la vista.
     * @throws ServletException Si ocurre una falla en el Servlet al reenviar la vista JSP.
     * @throws IOException Si ocurre un error de lectura/escritura durante el flujo de redirección HTTP.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // Verificar que exista una sesión
        if (session == null) {
            resp.sendRedirect("iniciar-sesion");
            return;
        }

        // Verificar que sea administrador
        String tipoUsuario = (String) session.getAttribute("tipo_usuario");

        if (!"ADMIN".equals(tipoUsuario)) {
            resp.sendRedirect("inicio");
            return;
        }

        // Mostrar la página del administrador
        req.getRequestDispatcher("InicioAdmin.jsp").forward(req, resp);
    }
}
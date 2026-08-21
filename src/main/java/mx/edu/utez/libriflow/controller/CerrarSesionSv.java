package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
/**
 * El servlet CerrarSesionSv gestiona la finalización de la sesión activa del usuario,
 * invalidando los datos guardados en la sesión y redirigiendo hacia la pantalla de login.
 *
 * @author Alejandro Mena Pereyda
 * @since 21/08/2026
 */
@WebServlet(name = "Cerrar_sesionSV", value = "/cerrar-sesion")
public class CerrarSesionSv extends HttpServlet {
    /**
     * El método doGet recupera la sesión actual, elimina el atributo de usuario,
     * invalida la sesión si existe y redirige al usuario a la vista "login.jsp".
     *
     * @author Alejandro Mena Pereyda
     * @since 21/08/2026
     *
     * @param request Objeto de solicitud HTTP.
     * @param response Objeto de respuesta HTTP para realizar la redirección.
     * @throws ServletException Si ocurre un error en el procesamiento del servlet.
     * @throws IOException Si ocurre un fallo en la redirección.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        System.out.println("entro");
        if (session != null) {
            session.removeAttribute("usuario");
            session.invalidate();
        }
        response.sendRedirect("login.jsp");
    }
}

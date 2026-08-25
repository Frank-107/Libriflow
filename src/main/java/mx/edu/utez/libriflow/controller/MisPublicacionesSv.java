package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.PublicacionResumen;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;
import java.util.List;

/**
 * Controlador Servlet encargado de la gestión de publicaciones de usuario.
 * Permite la visualización de las publicaciones propias registradas y la cancelación/eliminación
 * de una publicación en particular.
 *
 * @author Alejandro
 * @since 24/08/2026
 */


@WebServlet (name = "MisPublicacionesSv", value="/mis-publicaciones")
public class MisPublicacionesSv extends HttpServlet {
    PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();

    /**
     * Procesa la petición HTTP GET para obtener las publicaciones pertenecientes al usuario en sesión.
     * Recupera la sesión activa, obtiene el ID del usuario autenticado, realiza la consulta en la
     * base de datos mediante la capa DAO y redirige los resultados a la vista `MisPublicaciones.jsp`.
     *
     * @param req Objeto HttpServletRequest que contiene la sesión y datos de la solicitud HTTP.
     * @param resp Objeto HttpServletResponse para la gestión y reenvío de la respuesta.
     * @throws ServletException Si ocurre un error de ejecución en la estructura Servlet.
     * @throws IOException Si ocurre una falla de lectura/escritura durante el reenvío a la vista.
     *
     * @author Alejandro
     * @since 24/08/2026
     */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        int id = usuario.getId();
        List<PublicacionResumen> lista = publicacionUsuarioDao.getResumenPublicacionesPorUsuario(id);
        req.setAttribute("publicaciones",lista);

        req.getRequestDispatcher("MisPublicaciones.jsp").forward(req,resp);
    }

    /**
     * Procesa la petición HTTP POST para realizar acciones de modificación sobre las publicaciones.
     * Evalúa la acción enviada en los parámetros de la solicitud; en caso de ser "delete", procesa
     * la eliminación física o lógica de la publicación por ID y notifica el resultado a la vista.
     *
     * @param req Objeto HttpServletRequest con los parámetros de la acción e ID de la publicación.
     * @param resp Objeto HttpServletResponse para la respuesta de la petición.
     * @throws ServletException Si ocurre una falla en el Servlet.
     * @throws IOException Si ocurre un error de comunicación HTTP.
     *
     * @author Alejandro
     * @since 24/08/2026
     */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            try {
                int idPublicacion = Integer.parseInt(req.getParameter("idPublicacion"));
                publicacionUsuarioDao.deletePublicacionById(idPublicacion);
                req.setAttribute("exito", "La publicación se canceló correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "No se pudo cancelar la publicación. Inténtalo más tarde.");
            }
            doGet(req, resp);
        }
    }
}
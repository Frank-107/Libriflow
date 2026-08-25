package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.Dao.ResenaDao;
import mx.edu.utez.libriflow.model.PublicacionUsuarioCompleta;
import mx.edu.utez.libriflow.model.Resena;
import mx.edu.utez.libriflow.model.Usuario;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Este servlet se encarga de mostrar la información detallada de una publicación
 * realizada por un usuario. También permite consultar las reseñas relacionadas,
 * verificar si el usuario actual es propietario de la publicación y agregarla
 * al carrito.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 22/08/2026
 */
@WebServlet(name = "DetallePublicacionSv", value = "/detalle-publicacion")
public class DetallePublicacionSv extends HttpServlet {

    /**
     *
     * DAO utilizado para consultar la información completa de las publicaciones
     * realizadas por los usuarios.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private final PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();

    /**
     *
     * Este método se encarga de obtener la información completa de una publicación
     * mediante su identificador. También verifica si el usuario que tiene una sesión
     * activa es el propietario de la publicación y consulta las reseñas relacionadas.
     * Finalmente envía la información a la vista DetallePublicacion.jsp.
     *
     * @param req Contiene la solicitud HTTP y el identificador de la publicación
     *            que se desea consultar.
     * @param resp Permite generar la respuesta HTTP, realizar redirecciones y enviar
     *             la información hacia la vista correspondiente.
     *
     * @throws ServletException Si ocurre un problema al procesar o enviar la solicitud
     *                          hacia la vista.
     * @throws IOException Si ocurre un problema durante el manejo de la solicitud
     *                     o respuesta HTTP.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            int idPublicacion = Integer.parseInt(req.getParameter("idPublicacion"));

            PublicacionUsuarioCompleta publicacion =
                    publicacionUsuarioDao.getPublicacionUsuarioCompleta(idPublicacion);

            if (publicacion == null) {
                resp.sendRedirect(req.getContextPath() + "/inicio");
                return;
            }

            HttpSession session = req.getSession(false);
            boolean esPropietario = false;

            if (session != null && session.getAttribute("usuario") != null) {
                Usuario usuario = (Usuario) session.getAttribute("usuario");
                esPropietario = usuario.getId() == publicacion.getIdPropietario();
            }

            ResenaDao resenaDao = new ResenaDao();
            List<Resena> resenas = resenaDao.getResenasByPublicacion(idPublicacion);

            req.setAttribute("publicacion", publicacion);
            req.setAttribute("resenas", resenas);
            req.setAttribute("esPropietario", esPropietario);

            req.getRequestDispatcher("/DetallePublicacion.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/inicio");
        }
    }

    /**
     *
     * Este método se encarga de agregar una publicación al carrito almacenado
     * en la sesión del usuario. Obtiene el identificador de la publicación y,
     * si todavía no se encuentra dentro del carrito, lo agrega a la lista.
     * Finalmente redirige al usuario hacia la vista del carrito.
     *
     * @param req Contiene la solicitud HTTP y el identificador de la publicación
     *            que se desea agregar al carrito.
     * @param resp Permite generar la respuesta HTTP y redirigir al usuario
     *             hacia el carrito.
     *
     * @throws ServletException Si ocurre un problema durante el procesamiento
     *                          de la solicitud.
     * @throws IOException Si ocurre un problema durante el manejo de la solicitud
     *                     o respuesta HTTP.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idPublicacion = req.getParameter("idPublicacion");

        HttpSession session = req.getSession();

        ArrayList<Integer> carrito =
                (ArrayList<Integer>) session.getAttribute("carrito");

        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        int id = Integer.parseInt(idPublicacion);

        if (!carrito.contains(id)) {
            carrito.add(id);
        }

        session.setAttribute("carrito", carrito);

        resp.sendRedirect(req.getContextPath() + "/carrito");
    }
}
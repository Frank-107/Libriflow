package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.PublicacionUsuarioCompleta;
import mx.edu.utez.libriflow.model.Dao.ResenaDao;
import mx.edu.utez.libriflow.model.Resena;
import java.io.IOException;
import java.util.List;

/**
 *
 * Este servlet se encarga de mostrar la información detallada de una publicación
 * realizada por un usuario desde la vista del administrador. También permite
 * consultar las reseñas relacionadas con la publicación y realizar su baja
 * cuando el administrador lo solicita.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 22/08/2026
 */
@WebServlet(name = "DetallePublicacionUsForAdminSv", value = "/detalle-publicacion-us-admin")
public class DetallePublicacionUsForAdminSv extends HttpServlet {

    /**
     *
     * DAO utilizado para consultar y modificar la información de las publicaciones
     * realizadas por los usuarios.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();

    /**
     *
     * Este método se encarga de obtener la información completa de una publicación
     * realizada por un usuario mediante su identificador. También consulta las
     * reseñas relacionadas con la publicación y envía toda la información a la
     * vista DetallePublicacionAdmin.jsp.
     *
     * @param req Contiene la solicitud HTTP y el identificador de la publicación
     *            que se desea consultar.
     * @param resp Permite generar la respuesta HTTP y enviar la información
     *             hacia la vista correspondiente.
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int idPublicacion = Integer.parseInt(req.getParameter("idPublicacion"));
        PublicacionUsuarioCompleta publicacionUsuarioCompleta = publicacionUsuarioDao.getPublicacionUsuarioCompleta(idPublicacion);
        req.setAttribute("publicacion", publicacionUsuarioCompleta);

        ResenaDao resenaDao = new ResenaDao();
        List<Resena> resenas = resenaDao.getResenasByPublicacion(idPublicacion);
        req.setAttribute("resenas", resenas);

        req.getRequestDispatcher("/DetallePublicacionAdmin.jsp").forward(req, resp);
    }

    /**
     *
     * Este método se encarga de procesar la solicitud para dar de baja una
     * publicación realizada por un usuario. Valida que se reciba el identificador
     * de la publicación, realiza la operación mediante el DAO y posteriormente
     * redirige al administrador dependiendo del resultado obtenido.
     *
     * @param req Contiene la solicitud HTTP y el identificador de la publicación
     *            que se desea dar de baja.
     * @param resp Permite generar la respuesta HTTP y realizar la redirección
     *             correspondiente después de intentar dar de baja la publicación.
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idPublicacionStr = req.getParameter("idPublicacion");

        if (idPublicacionStr != null && !idPublicacionStr.isEmpty()) {
            int idPublicacion = Integer.parseInt(idPublicacionStr);

            boolean exito = publicacionUsuarioDao.darDeBajaPublicacionUsuario(idPublicacion);

            if (exito) {
                resp.sendRedirect("inicio-admin?exito=baja");
            } else {
                resp.sendRedirect("detalle-publicacion-us-admin?idPublicacion=" + idPublicacion + "&error=baja");
            }
        } else {
            resp.sendRedirect("inicio-admin");
        }
    }
}
package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.PublicacionAdministradorDao;
import mx.edu.utez.libriflow.model.Dao.ResenaDao;
import mx.edu.utez.libriflow.model.ItemCarritoAdmin;
import mx.edu.utez.libriflow.model.PublicacionAdminCompleta;
import mx.edu.utez.libriflow.model.Resena;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Este servlet se encarga de mostrar el detalle de las publicaciones pertenecientes
 * a LibriFlow y gestionar su incorporación al carrito de compras o rentas.
 * También permite consultar las reseñas de la publicación y verificar si el usuario
 * tiene permitido realizar una reseña.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 22/08/2026
 */
@WebServlet(name = "DetallePublicacionAdminSv", value = "/detalle-publicacion-superad")
public class DetallePublicacionAdminSv extends HttpServlet {

    /**
     *
     * DAO utilizado para consultar la información completa de las publicaciones
     * administradas por LibriFlow.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private final PublicacionAdministradorDao publicacionAdminDao = new PublicacionAdministradorDao();

    /**
     *
     * DAO utilizado para consultar las reseñas de una publicación y verificar
     * si un usuario ha comprado o rentado un libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private final ResenaDao resenaDao = new ResenaDao();

    /**
     *
     * Este método se encarga de obtener la información completa de una publicación
     * de LibriFlow y sus reseñas. También verifica si el usuario que tiene una sesión
     * activa ha comprado o rentado la publicación para determinar si puede realizar
     * una reseña. Finalmente envía la información obtenida a la vista
     * DetallePublicacion.jsp.
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
        PublicacionAdminCompleta publicacionAdminCompleta = publicacionAdminDao.getPublicacionAdminCompleta(idPublicacion);

        List<Resena> resenas = resenaDao.getResenasByPublicacion(idPublicacion);
        req.setAttribute("resenas", resenas);

        HttpSession session = req.getSession(false);
        boolean haCompradoORentado = false;
        if (session != null && session.getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            haCompradoORentado = resenaDao.usuarioHaCompradoORentado(usuario.getId(), idPublicacion);
        }
        req.setAttribute("haCompradoORentado", haCompradoORentado);

        req.setAttribute("publicacion", publicacionAdminCompleta);
        req.setAttribute("esAdminPub", true);

        req.getRequestDispatcher("/DetallePublicacionJS.jsp").forward(req, resp);
    }

    /**
     *
     * Este método se encarga de agregar una publicación de LibriFlow al carrito.
     * Obtiene el tipo de operación y el precio calculado y, cuando se trata de una
     * renta, también procesa las fechas de inicio y finalización. Si la publicación
     * ya se encuentra en el carrito actualiza su información y, en caso contrario,
     * crea un nuevo elemento y lo agrega al carrito de la sesión.
     *
     * @param req Contiene la solicitud HTTP con la información de la publicación,
     *            el tipo de operación, el precio y las fechas de renta cuando
     *            corresponda.
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idPublicacionStr = req.getParameter("idPublicacion");
        String tipoOperacion = req.getParameter("tipoOperacion");
        System.out.println("Tipo de operación: " + tipoOperacion);
        String precioCalculadoStr = req.getParameter("precioCalculado");

        Timestamp fechaFin = null;
        Timestamp fechaInicio = null;
        if (tipoOperacion.equals("renta")) {

            String fechaInicioStr = req.getParameter("fechaInicio");

            LocalDate fechaInicioL = LocalDate.parse(fechaInicioStr);

            fechaInicio = Timestamp.valueOf(fechaInicioL.atStartOfDay());

            String fechaFinStr = req.getParameter("fechaFin");

            LocalDate fechaFinL = LocalDate.parse(fechaFinStr);

            fechaFin = Timestamp.valueOf(fechaFinL.atStartOfDay());
        }

        int idPublicacion = Integer.parseInt(idPublicacionStr);
        double precioFinal = 0.0;
        if (precioCalculadoStr != null && !precioCalculadoStr.isEmpty()) {
            precioFinal = Double.parseDouble(precioCalculadoStr);
        }

        HttpSession session = req.getSession();
        List<ItemCarritoAdmin> carritoAdmin = (List<ItemCarritoAdmin>) session.getAttribute("carritoAdmin");
        if (carritoAdmin == null) {
            carritoAdmin = new ArrayList<>();
        }

        boolean existe = false;
        for (ItemCarritoAdmin item : carritoAdmin) {
            if (item.getIdPublicacion() == idPublicacion) {
                item.setTipoOperacion(tipoOperacion);
                item.setPrecio(precioFinal);

                existe = true;
                break;
            }
        }

        if (!existe) {
            ItemCarritoAdmin itemNuevo = new ItemCarritoAdmin(idPublicacion, tipoOperacion, precioFinal);
            if(tipoOperacion.equals("renta")){
                itemNuevo.setFechaInicio(fechaInicio);
                itemNuevo.setFechaFin(fechaFin);
            }
            carritoAdmin.add(itemNuevo);
        }

        session.setAttribute("carritoAdmin", carritoAdmin);

        resp.sendRedirect("carrito");
    }
}
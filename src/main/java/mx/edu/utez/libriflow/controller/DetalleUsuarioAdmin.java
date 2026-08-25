package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.libriflow.model.Dao.*;
import mx.edu.utez.libriflow.model.Movimiento;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;
import java.util.List;

/**
 *
 * Este servlet se encarga de mostrar al administrador la información detallada
 * de un usuario. Permite consultar sus datos, movimientos realizados y diferentes
 * contadores relacionados con sus publicaciones, ventas, rentas y retrasos.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 22/08/2026
 */
@WebServlet(name = "DetalleUsuarioAdmin", value = "/detalle-usuario-admin")
public class DetalleUsuarioAdmin extends HttpServlet {

    /**
     *
     * DAO utilizado para consultar información relacionada con las publicaciones
     * realizadas por los usuarios.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();

    /**
     *
     * DAO utilizado para consultar información relacionada con las compras
     * y ventas realizadas dentro del sistema.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    CompraDao compraDao = new CompraDao();

    /**
     *
     * DAO utilizado para consultar información relacionada con las rentas
     * y retrasos de los usuarios.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    RentaDao rentaDao = new RentaDao();

    /**
     *
     * Este método recibe solicitudes mediante POST. Actualmente no contiene
     * ninguna operación implementada.
     *
     * @param req Contiene la solicitud HTTP recibida.
     * @param resp Permite generar la respuesta HTTP.
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

    }

    /**
     *
     * Este método se encarga de obtener la información detallada de un usuario
     * mediante su identificador. Consulta los datos del usuario, carga los
     * contadores relacionados con su actividad y obtiene la lista de movimientos
     * realizados. Finalmente envía toda la información a la vista
     * DetalleUsuarioAdmin.jsp.
     *
     * @param req Contiene la solicitud HTTP y el identificador del usuario
     *            que se desea consultar.
     * @param resp Permite generar la respuesta HTTP y enviar la información
     *             hacia la vista correspondiente.
     *
     * @throws ServletException Si ocurre un problema al procesar o enviar la
     *                          solicitud hacia la vista.
     * @throws IOException Si ocurre un problema durante el manejo de la solicitud
     *                     o respuesta HTTP.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UsuarioDao usuarioDao = new UsuarioDao();
        DetalleTransaccionDao detalleTransaccionDao = new DetalleTransaccionDao();
        int idUsuario = Integer.parseInt(req.getParameter("idUsuario"));
        Usuario usuario = usuarioDao.getById(idUsuario);

        //definir todos los contadores con los metodos que hara monse
        cargarContadores(req,idUsuario);

        List<Movimiento> movimientos = detalleTransaccionDao.getMovimientosByIdUsuario(idUsuario);



        req.setAttribute("movimiento", movimientos);
        req.setAttribute("cantidadMovimientos", movimientos.size());
        req.setAttribute("usuario", usuario);
        req.getRequestDispatcher("DetalleUsuarioAdmin.jsp").forward(req, resp);

    }

    /**
     *
     * Este método se encarga de obtener los diferentes contadores relacionados
     * con la actividad de un usuario. Consulta el total de publicaciones, ventas,
     * rentas activas y retrasos, y almacena estos valores como atributos de la
     * solicitud para que puedan ser utilizados en la vista.
     *
     * @param req Contiene la solicitud HTTP en la que se almacenarán los
     *            contadores obtenidos.
     * @param idUsuario Es el identificador del usuario del cual se desean
     *                  consultar los contadores.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private void cargarContadores(HttpServletRequest req, int idUsuario) {
        int totalPublicaciones = publicacionUsuarioDao.contarPublicacionesPorUsuario(idUsuario);
        int totalVendidos = compraDao.contarVentasPorUsuario(idUsuario);
        int totalEnRenta = rentaDao.contarRentasActivasPorUsuario(idUsuario);
        int totalRetrasos = rentaDao.contarRetrasosPorUsuario(idUsuario);

        req.setAttribute("totalPublicaciones", totalPublicaciones);
        req.setAttribute("totalVendidos", totalVendidos);
        req.setAttribute("totalEnRenta", totalEnRenta);
        req.setAttribute("totalRetrasos", totalRetrasos);
    }
}
package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.libriflow.model.Dao.RentaDao;
import mx.edu.utez.libriflow.model.RentaResumen;

import java.io.IOException;
import java.util.List;

/**
 * Controlador Servlet enfocado en la administración general del flujo de rentas de la plataforma.
 * Permite al administrador visualizar todas las rentas registradas en el sistema y actualizar
 * el estado operativo de cada entrega o devolución.
 *
 * @author Andres
 * @since 24/08/2026
 */

@WebServlet(name = "MisRentasAdminSv", value = "/mis-rentas-admin")
public class MisRentasAdminSv extends HttpServlet {

    private final RentaDao rentaDao = new RentaDao();

    /**
     * Procesa la petición HTTP GET para cargar el tablero global de rentas administrativas.
     * Recupera el listado completo de rentas sin filtro de usuario mediante el DAO y
     * reenvía los datos a la vista `MisRentasAdmin.jsp`.
     *
     * @param req Objeto HttpServletRequest con la solicitud HTTP.
     * @param resp Objeto HttpServletResponse para manejar la redirección o despacho.
     * @throws ServletException Si ocurre una falla en el Servlet durante el despacho.
     * @throws IOException Si ocurre un error de lectura o escritura en el flujo HTTP.
     *
     * @author Andres
     * @since 24/08/2026
     */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<RentaResumen> lista =
                rentaDao.getResumenTodasLasRentas();

        req.setAttribute("rentas", lista);

        req.getRequestDispatcher("MisRentasAdmin.jsp")
                .forward(req, resp);
    }

    /**
     * Procesa la petición HTTP POST para actualizar el estado del ciclo de vida de una renta.
     * Recibe el identificador del detalle de la renta y la acción deseada ('ENTREGAR' o 'DEVOLVER'),
     * ejecutando los cambios de estado correspondientes a través del DAO antes de redireccionar.
     *
     * @param req Objeto HttpServletRequest con los parámetros 'idDetalle' y 'accion'.
     * @param resp Objeto HttpServletResponse utilizado para redirigir de vuelta al tablero.
     * @throws ServletException Si ocurre un error interno en la ejecución del Servlet.
     * @throws IOException Si ocurre una falla en el envío de la redirección HTTP.
     *
     * @author Andres
     * @since 24/08/2026
     */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int idDetalle =
                Integer.parseInt(req.getParameter("idDetalle"));

        String accion =
                req.getParameter("accion");

        if ("ENTREGAR".equals(accion)) {

            rentaDao.marcarComoEntregada(idDetalle);

        } else if ("DEVOLVER".equals(accion)) {

            rentaDao.marcarComoFinalizada(idDetalle);
        }

        resp.sendRedirect(
                req.getContextPath() + "/mis-rentas-admin"
        );
    }
}
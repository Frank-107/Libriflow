package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.libriflow.model.Dao.DetalleTransaccionDao;
import mx.edu.utez.libriflow.model.Movimiento;

import java.io.IOException;
import java.util.List;
/**
 * El servlet AdminIngresosSv gestiona la consulta y visualización de los
 * ingresos y movimientos financieros dentro del panel de administración.
 *
 * @author Francisco Emmanuel Fuentes Perez
 * @since 21/08/2026
 */
@WebServlet(name = "AdminIngresosSv", value = "/ingresos-admin")
public class AdminIngresosSv extends HttpServlet {
    DetalleTransaccionDao detalleTransaccionDao = new DetalleTransaccionDao();
    /**
     * El método doGet recupera el listado completo de ingresos y movimientos
     * transaccionales desde la base de datos y asigna la información a la
     * vista "AdminIngresos.jsp".
     *
     * @author Francisco Emmanuel Fuentes Perez
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP.
     * @param resp Objeto de respuesta HTTP.
     * @throws ServletException Si ocurre un error en el despacho hacia la vista JSP.
     * @throws IOException Si ocurre un fallo en la comunicación de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Movimiento> ingresos = detalleTransaccionDao.getAllMovimientosIngresos();
        req.setAttribute("ingresos", ingresos);
        req.getRequestDispatcher("AdminIngresos.jsp").forward(req, resp);
    }
    /**
     * El método doPost procesa las peticiones de tipo POST delegando su comportamiento
     * a la implementación base de HttpServlet.
     *
     * @author Francisco Emmanuel Fuentes Perez
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP.
     * @param resp Objeto de respuesta HTTP.
     * @throws ServletException Si ocurre un error en el procesamiento de la petición.
     * @throws IOException Si ocurre un fallo de lectura o escritura.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}

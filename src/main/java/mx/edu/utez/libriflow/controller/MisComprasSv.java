package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.CompraResumen;
import mx.edu.utez.libriflow.model.Dao.CompraDao;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;
import java.util.List;

/**
 * Controlador Servlet encargado de gestionar la consulta y visualización del historial de compras del usuario.
 * Recupera las compras realizadas por el usuario autenticado y las envía a la vista correspondiente.
 *
 * @author Andres
 * @since 24/08/2026
 */

@WebServlet(name = "MisComprasSv", value = "/mis-compras")
public class MisComprasSv extends HttpServlet {

    CompraDao compraDao = new CompraDao();

    /**
     * Procesa la petición HTTP GET para obtener el listado histórico de compras del usuario en sesión.
     * Extrae el identificador del usuario autenticado, consulta el objeto de acceso a datos (DAO)
     * para obtener las compras registradas y redirige el flujo hacia la vista `MisCompras.jsp`.
     *
     * @param req Objeto HttpServletRequest que contiene la información de la solicitud y sesión HTTP.
     * @param resp Objeto HttpServletResponse para gestionar la respuesta o despacho de la vista.
     * @throws ServletException Si ocurre una falla en la ejecución interna del Servlet.
     * @throws IOException Si ocurre un error de lectura o escritura durante el reenvío de la petición.
     *
     * @author Andres
     * @since 24/08/2026
     */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession sesion = req.getSession(false);
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        int idUsuario = usuario.getId();

        List<CompraResumen> lista = compraDao.getResumenComprasPorUsuario(idUsuario);
        req.setAttribute("compras", lista);

        req.getRequestDispatcher("MisCompras.jsp").forward(req, resp);
    }
}
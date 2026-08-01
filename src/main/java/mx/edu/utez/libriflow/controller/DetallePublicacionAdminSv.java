package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.PublicacionAdministradorDao;
import mx.edu.utez.libriflow.model.ItemCarritoAdmin;
import mx.edu.utez.libriflow.model.PublicacionAdminCompleta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "DetallePublicacionAdminSv", value = "/detalle-publicacion-admin")
public class DetallePublicacionAdminSv extends HttpServlet {

    private final PublicacionAdministradorDao publicacionAdminDao = new PublicacionAdministradorDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int idPublicacion = Integer.parseInt(req.getParameter("idPublicacion"));
        PublicacionAdminCompleta publicacionAdminCompleta = publicacionAdminDao.getPublicacionAdminCompleta(idPublicacion);

        req.setAttribute("publicacion", publicacionAdminCompleta);
        req.setAttribute("esAdminPub", true);

        req.getRequestDispatcher("/DetallePublicacion.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idPublicacionStr = req.getParameter("idPublicacion");
        String tipoOperacion = req.getParameter("tipoOperacion");
        String precioCalculadoStr = req.getParameter("precioCalculado");

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
            carritoAdmin.add(new ItemCarritoAdmin(idPublicacion, tipoOperacion, precioFinal));
        }

        session.setAttribute("carritoAdmin", carritoAdmin);

        resp.sendRedirect("carrito");
    }
}
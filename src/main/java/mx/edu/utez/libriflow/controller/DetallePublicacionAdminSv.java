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

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "DetallePublicacionAdminSv", value = "/detalle-publicacion-superad")
public class DetallePublicacionAdminSv extends HttpServlet {

    private final PublicacionAdministradorDao publicacionAdminDao = new PublicacionAdministradorDao();
    private final ResenaDao resenaDao = new ResenaDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int idPublicacion = Integer.parseInt(req.getParameter("idPublicacion"));
        PublicacionAdminCompleta publicacionAdminCompleta = publicacionAdminDao.getPublicacionAdminCompleta(idPublicacion);

        List<Resena> resenas = resenaDao.getResenasByPublicacion(idPublicacion);
        req.setAttribute("resenas", resenas);

        req.setAttribute("publicacion", publicacionAdminCompleta);
        req.setAttribute("esAdminPub", true);

        req.getRequestDispatcher("/DetallePublicacion.jsp").forward(req, resp);
    }

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
//          sout para ver los objetos añadidos al carrito
//        for(ItemCarritoAdmin item : carritoAdmin){
//            System.out.println("ID Publicación: " + item.getIdPublicacion() + ", Tipo de Operación: " + item.getTipoOperacion() + ", Precio: " + item.getPrecio());
//            if(tipoOperacion.equals("renta")){
//                System.out.println("Fecha de Inicio: " + item.getFechaInicio());
//                System.out.println("Fecha de Fin: " + item.getFechaFin());
//            }
//        }

        resp.sendRedirect("carrito");
    }
}
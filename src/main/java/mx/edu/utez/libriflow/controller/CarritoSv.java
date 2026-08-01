package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.PublicacionAdministradorDao;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.ItemCarritoAdmin;
import mx.edu.utez.libriflow.model.PublicacionAdminCompleta;
import mx.edu.utez.libriflow.model.PublicacionResumen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CarritoSv", value = "/carrito")
public class CarritoSv extends HttpServlet {

    private final PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();
    private final PublicacionAdministradorDao adminDao = new PublicacionAdministradorDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        List<PublicacionResumen> publicaciones = new ArrayList<>();

        List<Integer> idsPublicaciones = (List<Integer>) session.getAttribute("carrito");
        if (idsPublicaciones != null && !idsPublicaciones.isEmpty()) {
            List<PublicacionResumen> pubsUsuario = publicacionUsuarioDao.getPublicacionesByArreglo(idsPublicaciones);
            if (pubsUsuario != null) {
                publicaciones.addAll(pubsUsuario);
            }
        }

        List<ItemCarritoAdmin> carritoAdmin = (List<ItemCarritoAdmin>) session.getAttribute("carritoAdmin");
        if (carritoAdmin != null && !carritoAdmin.isEmpty()) {
            for (ItemCarritoAdmin item : carritoAdmin) {
                PublicacionAdminCompleta adminPub = adminDao.getPublicacionAdminCompleta(item.getIdPublicacion());

                if (adminPub != null) {
                    PublicacionResumen resumenAdmin = new PublicacionResumen();
                    resumenAdmin.setIdPublicacion(adminPub.getIdPublicacionLf());
                    resumenAdmin.setTitulo(adminPub.getTitulo());
                    resumenAdmin.setAutor(adminPub.getAutor());
                    resumenAdmin.setGenero(adminPub.getGenero());
                    resumenAdmin.setImagenPrincipal(adminPub.getImagenPrincipal());
                    resumenAdmin.setEsLibriFlow(true);

                    if ("renta".equals(item.getTipoOperacion())) {
                        resumenAdmin.setPrecio(item.getPrecio());
                        resumenAdmin.setEsRentaSeleccionada(true);
                    } else {
                        resumenAdmin.setPrecio(adminPub.getPrecio());
                        resumenAdmin.setEsRentaSeleccionada(false);
                    }

                    publicaciones.add(resumenAdmin);
                }
            }
        }

        req.setAttribute("publicaciones", publicaciones);
        req.getRequestDispatcher("Carrito.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("eliminar".equals(action)) {
            int idEliminar = Integer.parseInt(req.getParameter("idPublicacion"));
            HttpSession session = req.getSession();

            boolean eliminadoDeUsuario = false;
            List<Integer> ids = (List<Integer>) session.getAttribute("carrito");
            if (ids != null && ids.contains(Integer.valueOf(idEliminar))) {
                ids.remove(Integer.valueOf(idEliminar));
                session.setAttribute("carrito", ids);
                eliminadoDeUsuario = true;
            }

            if (!eliminadoDeUsuario) {
                List<ItemCarritoAdmin> carritoAdmin = (List<ItemCarritoAdmin>) session.getAttribute("carritoAdmin");
                if (carritoAdmin != null) {
                    carritoAdmin.removeIf(item -> item.getIdPublicacion() == idEliminar);
                    session.setAttribute("carritoAdmin", carritoAdmin);
                }
            }

            resp.sendRedirect("carrito");
        } else if ("comprar".equals(action)) {
            resp.sendRedirect("validar-tarjeta");
        }
    }
}
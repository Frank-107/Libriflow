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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        HttpSession session = req.getSession(false);

        if ("comprar".equals(action)) {
            session.setAttribute("puedeDireccion", true);

            boolean tieneCompras = Boolean.parseBoolean(req.getParameter("contieneEnvio"));
            double subtotal = Double.parseDouble(req.getParameter("subtotal"));

            session.setAttribute("subtotal", subtotal);

            if (tieneCompras) {
                session.setAttribute("puedePagar", true);
                resp.sendRedirect("direccion-envio");
                return;
            }

            session.setAttribute("envio", 0.0);
            session.setAttribute("puedePagar", true);
            resp.sendRedirect("validar-tarjeta");
            return;
        }

        if ("eliminar".equals(action)) {
            int idEliminar = Integer.parseInt(req.getParameter("idPublicacion"));
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
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }
}

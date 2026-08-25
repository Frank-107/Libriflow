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

/**
 * El servlet CarritoSv gestiona la visualización, cálculo y eliminación de ítems
 * dentro del carrito de compras, abarcando publicaciones tanto de usuarios como
 * de la administración (compras y rentas).
 *
 * @author Francisco Emmanuel Fuentes Perez
 * @author Alejandro Mena Pereyda
 * @since 21/08/2026
 */
@WebServlet(name = "CarritoSv", value = "/carrito")
public class CarritoSv extends HttpServlet {

    private final PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();
    private final PublicacionAdministradorDao adminDao = new PublicacionAdministradorDao();

    /**
     * El método doGet obtiene la lista de publicaciones almacenadas en la sesión activa
     * (tanto para usuarios como de administración), consolida sus datos de venta o renta
     * y transfiere la lista a la vista "Carrito.jsp".
     *
     * @author Francisco Emmanuel Fuentes Perez
     * @author Alejandro Mena Pereyda
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP.
     * @param resp Objeto de respuesta HTTP.
     * @throws ServletException Si ocurre un error en el despacho hacia la vista JSP.
     * @throws IOException Si ocurre un fallo en la comunicación de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        List<PublicacionResumen> publicaciones = new ArrayList<>();

        // 1. Procesar publicaciones de usuarios (Carrito tradicional)
        List<Integer> idsPublicaciones = (List<Integer>) session.getAttribute("carrito");

        if (idsPublicaciones != null && !idsPublicaciones.isEmpty()) {
            List<PublicacionResumen> pubsUsuario = publicacionUsuarioDao.getPublicacionesByArreglo(idsPublicaciones);
            if (pubsUsuario != null) {
                publicaciones.addAll(pubsUsuario);
            }
        }

        // 2. Procesar publicaciones de administración (Carrito Admin)
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

                    // Asignar precio y fechas según el tipo de operación
                    if ("renta".equalsIgnoreCase(item.getTipoOperacion())) {
                        resumenAdmin.setPrecio(item.getPrecio());
                        resumenAdmin.setEsRentaSeleccionada(true);

                        // Asignación de timestamps provenientes del ItemCarritoAdmin
                        resumenAdmin.setFechaInicio(item.getFechaInicio());
                        resumenAdmin.setFechaFin(item.getFechaFin());
                    } else {
                        resumenAdmin.setPrecio(adminPub.getPrecio());
                        resumenAdmin.setEsRentaSeleccionada(false);
                    }

                    publicaciones.add(resumenAdmin);
                }
            }
        }

        // Guardar la lista construida en el request para enviarla a la vista JSP
        req.setAttribute("publicaciones", publicaciones);

        // Redirigir a la vista del carrito (ajusta la ruta según tu estructura)
        req.getRequestDispatcher("Carrito.jsp").forward(req, resp);
    }

    /**
     * El método doPost gestiona las acciones de compra y eliminación de productos
     * del carrito, configurando las banderas de sesión necesarias para el proceso de checkout.
     *
     * @author Francisco Emmanuel Fuentes Perez
     * @author Alejandro Mena Pereyda
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP que contiene los datos del formulario ("action").
     * @param resp Objeto de respuesta HTTP para efectuar la redirección.
     * @throws ServletException Si ocurre un fallo en el procesamiento.
     * @throws IOException Si ocurre un error de redirección o entrada/salida.
     */
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

    /**
     * El método doDelete canaliza las peticiones de eliminación HTTP DELETE
     * invocando la implementación base de HttpServlet.
     *
     * @author Francisco Emmanuel Fuentes Perez
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP.
     * @param resp Objeto de respuesta HTTP.
     * @throws ServletException Si ocurre un fallo interno en el servlet.
     * @throws IOException Si ocurre un error de lectura o escritura.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
    /**
     * El método doPut canaliza las peticiones de actualización HTTP PUT
     * invocando la implementación base de HttpServlet.
     *
     * @author Francisco Emmanuel Fuentes Perez
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP.
     * @param resp Objeto de respuesta HTTP.
     * @throws ServletException Si ocurre un fallo interno en el servlet.
     * @throws IOException Si ocurre un error de lectura o escritura.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }
}

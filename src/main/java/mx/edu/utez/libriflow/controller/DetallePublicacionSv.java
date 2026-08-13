package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.Dao.ResenaDao;
import mx.edu.utez.libriflow.model.PublicacionUsuarioCompleta;
import mx.edu.utez.libriflow.model.Resena;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "DetallePublicacionSv", value = "/detalle-publicacion")
public class DetallePublicacionSv extends HttpServlet {

    private final PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            int idPublicacion = Integer.parseInt(req.getParameter("idPublicacion"));

            PublicacionUsuarioCompleta publicacion =
                    publicacionUsuarioDao.getPublicacionUsuarioCompleta(idPublicacion);

            if (publicacion == null) {
                resp.sendRedirect(req.getContextPath() + "/inicio");
                return;
            }

            HttpSession session = req.getSession(false);
            boolean esPropietario = false;

            if (session != null && session.getAttribute("usuario") != null) {
                Usuario usuario = (Usuario) session.getAttribute("usuario");
                esPropietario = usuario.getId() == publicacion.getIdPropietario();
            }

            ResenaDao resenaDao = new ResenaDao();
            List<Resena> resenas = resenaDao.getResenasByPublicacion(idPublicacion);

            req.setAttribute("publicacion", publicacion);
            req.setAttribute("resenas", resenas);
            req.setAttribute("esPropietario", esPropietario);

            req.getRequestDispatcher("/DetallePublicacion.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/inicio");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idPublicacion = req.getParameter("idPublicacion");

        HttpSession session = req.getSession();

        ArrayList<Integer> carrito =
                (ArrayList<Integer>) session.getAttribute("carrito");

        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        int id = Integer.parseInt(idPublicacion);

        if (!carrito.contains(id)) {
            carrito.add(id);
        }

        session.setAttribute("carrito", carrito);

        resp.sendRedirect(req.getContextPath() + "/carrito");
    }
}
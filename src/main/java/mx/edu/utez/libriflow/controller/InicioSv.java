package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.libriflow.model.Dao.PublicacionAdministradorDao;
import mx.edu.utez.libriflow.model.Dao.PublicacionUsuarioDao;
import mx.edu.utez.libriflow.model.PublicacionResumen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "InicioSv", value = "/inicio")
public class InicioSv extends HttpServlet {
    private final PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();
    private final PublicacionAdministradorDao publicacionAdministradorDao = new PublicacionAdministradorDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String busqueda = req.getParameter("q");
        String genero = req.getParameter("genero");

        List<PublicacionResumen> publicacionesUs = publicacionUsuarioDao.buscarYFiltrarPublicacionesUs("ACTIVO", busqueda, genero);

        List<PublicacionResumen> publicacionesAdmin = publicacionAdministradorDao.getResumenCatalogo();

        List<PublicacionResumen> catalogo = new ArrayList<>();

        if (publicacionesAdmin != null) {
            for (PublicacionResumen adminPub : publicacionesAdmin) {
                boolean cumpleBusqueda = true;
                boolean cumpleGenero = true;

                if (busqueda != null && !busqueda.trim().isEmpty()) {
                    String q = busqueda.trim().toLowerCase();
                    String titulo = adminPub.getTitulo() != null ? adminPub.getTitulo().toLowerCase() : "";
                    String autor = adminPub.getAutor() != null ? adminPub.getAutor().toLowerCase() : "";
                    cumpleBusqueda = titulo.contains(q) || autor.contains(q);
                }

                if (genero != null && !genero.trim().isEmpty() && !genero.equalsIgnoreCase("TODOS")) {
                    String g = adminPub.getGenero() != null ? adminPub.getGenero().toLowerCase() : "";
                    cumpleGenero = g.equalsIgnoreCase(genero.trim());
                }if (cumpleBusqueda && cumpleGenero) {
                    catalogo.add(adminPub);
                }
            }
        }
        if (publicacionesUs != null) {
            catalogo.addAll(publicacionesUs);
        }
        if ((busqueda == null || busqueda.trim().isEmpty()) && (genero == null || genero.trim().isEmpty())) {
            Collections.shuffle(catalogo);
        }
        req.setAttribute("publicaciones", catalogo);
        req.setAttribute("paramBusqueda", busqueda);
        req.setAttribute("paramGenero", genero);

        req.getRequestDispatcher("Inicio.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}

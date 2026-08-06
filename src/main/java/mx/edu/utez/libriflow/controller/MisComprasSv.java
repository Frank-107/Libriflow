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

@WebServlet(name = "MisComprasSv", value = "/mis-compras")
public class MisComprasSv extends HttpServlet {

    CompraDao compraDao = new CompraDao();

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
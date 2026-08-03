package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.libriflow.model.Dao.RentaDao;
import mx.edu.utez.libriflow.model.RentaResumen;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MisRentasSv", value = "/mis-rentas")
public class MisRentasSv extends HttpServlet {

    RentaDao rentaDao = new RentaDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession sesion = req.getSession(false);
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        int idUsuario = usuario.getId();

        List<RentaResumen> lista = rentaDao.getResumenRentasPorUsuario(idUsuario);
        req.setAttribute("rentas", lista);

        req.getRequestDispatcher("MisRentas.jsp").forward(req, resp);
    }
}
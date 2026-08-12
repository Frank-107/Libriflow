package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.libriflow.model.Dao.*;
import mx.edu.utez.libriflow.model.DetalleRenta;
import mx.edu.utez.libriflow.model.DetalleTransaccion;
import mx.edu.utez.libriflow.model.Movimiento;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "DetalleUsuarioAdmin", value = "/detalle-usuario-admin")
public class DetalleUsuarioAdmin extends HttpServlet {
    PublicacionUsuarioDao publicacionUsuarioDao = new PublicacionUsuarioDao();
    CompraDao compraDao = new CompraDao();
    RentaDao rentaDao = new RentaDao();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UsuarioDao usuarioDao = new UsuarioDao();
        DetalleTransaccionDao detalleTransaccionDao = new DetalleTransaccionDao();
        int idUsuario = Integer.parseInt(req.getParameter("idUsuario"));
        Usuario usuario = usuarioDao.getById(idUsuario);

        //definir todos los contadores con los metodos que hara monse
        cargarContadores(req,idUsuario);

        List<Movimiento> movimientos = detalleTransaccionDao.getMovimientosByIdUsuario(idUsuario);



        req.setAttribute("movimiento", movimientos);
        req.setAttribute("cantidadMovimientos", movimientos.size());
        req.setAttribute("usuario", usuario);
        req.getRequestDispatcher("DetalleUsuarioAdmin.jsp").forward(req, resp);

    }

    private void cargarContadores(HttpServletRequest req, int idUsuario) {
        int totalPublicaciones = publicacionUsuarioDao.contarPublicacionesPorUsuario(idUsuario);
        int totalVendidos = compraDao.contarVentasPorUsuario(idUsuario);
        int totalEnRenta = rentaDao.contarRentasActivasPorUsuario(idUsuario);
        int totalRetrasos = rentaDao.contarRetrasosPorUsuario(idUsuario);

        req.setAttribute("totalPublicaciones", totalPublicaciones);
        req.setAttribute("totalVendidos", totalVendidos);
        req.setAttribute("totalEnRenta", totalEnRenta);
        req.setAttribute("totalRetrasos", totalRetrasos);
    }
}

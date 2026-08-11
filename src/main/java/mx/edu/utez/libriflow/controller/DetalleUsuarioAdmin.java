package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.libriflow.model.Dao.DetalleTransaccionDao;
import mx.edu.utez.libriflow.model.Dao.UsuarioDao;
import mx.edu.utez.libriflow.model.DetalleRenta;
import mx.edu.utez.libriflow.model.DetalleTransaccion;
import mx.edu.utez.libriflow.model.Movimiento;
import mx.edu.utez.libriflow.model.Usuario;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "DetalleUsuarioAdmin", value = "/detalle-usuario-admin")
public class DetalleUsuarioAdmin extends HttpServlet {
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
        int cantidadPublicaciones = 0;
        int cantidadVentas = 0;
        int cantidadRentasActivas = 0;
        int cantidadRetrasos = 0;

        List<Movimiento> movimientos = detalleTransaccionDao.getMovimientosByIdUsuario(idUsuario);







        req.setAttribute("cantidadPublicaciones", cantidadPublicaciones);
        req.setAttribute("cantidadVentas", cantidadVentas);
        req.setAttribute("cantidadRentasActivas", cantidadRentasActivas);
        req.setAttribute("cantidadRetrasos", cantidadRetrasos);

        req.setAttribute("movimiento", movimientos);
        req.setAttribute("cantidadMovimientos", movimientos.size());
        req.setAttribute("usuario", usuario);
        req.getRequestDispatcher("DetalleUsuarioAdmin.jsp").forward(req, resp);

    }
}

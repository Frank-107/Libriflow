package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.libriflow.model.Dao.UsuarioDao;

import java.io.IOException;
/**
 * El servlet AdminDesbloquearUsuario permite a los administradores reactivar
 * la cuenta de un usuario cambiando su estado a "ACTIVA".
 *
 * @author Francisco Emmanuel Fuentes Perez
 * @since 21/08/2026
 */
@WebServlet(name = "AdminDesbloquearUsuario", value = "/admin-desbloquear-usuario")
public class AdminDesbloquearUsuario extends HttpServlet {

    /**
     * El método doPost procesa la solicitud para reactivar la cuenta de un usuario.
     * Obtiene el identificador recibido por parámetro, ejecuta el cambio de estado
     * a "ACTIVA" en la base de datos y reorienta la navegación al detalle del usuario.
     *
     * @author Francisco Emmanuel Fuentes Perez
     * @since 21/08/2026
     *
     * @param req Objeto de solicitud HTTP con el identificador 'idUsuario'.
     * @param resp Objeto de respuesta HTTP para efectuar la redirección.
     * @throws ServletException Si ocurre un fallo en la ejecución del servlet.
     * @throws IOException Si ocurre un error de comunicación de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UsuarioDao usuarioDao = new UsuarioDao();

        int idUsuario = Integer.parseInt(req.getParameter("idUsuario"));

        boolean desbloqueado = usuarioDao.cambiarEstadoUsuario(
                idUsuario,
                "ACTIVA"
        );

        if (!desbloqueado) {
            System.err.println("Error al desbloquear usuario: " + idUsuario);
        } else {
            System.out.println("Usuario " + idUsuario + " desbloqueado correctamente.");
        }

        resp.sendRedirect(
                "detalle-usuario-admin?idUsuario=" + idUsuario
        );
    }
}
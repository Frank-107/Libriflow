package mx.edu.utez.libriflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.libriflow.model.Dao.UsuarioDao;

import java.io.IOException;
/**
 * El servlet AdminBloquearUsuario permite a los administradores cambiar el estado
 * de una cuenta de usuario a "INACTIVA", restringiendo su acceso a la plataforma.
 *
 * @author Francisco Emmanuel Fuentes Perez
 * @since 21/08/2026
 */
@WebServlet(name = "AdminBloquearUsuario", value = "/admin-bloquear-usuario")
public class AdminBloquearUsuario extends HttpServlet {

    /**
     * El método doPost procesa la solicitud para inhabilitar a un usuario. Obtiene el
     * identificador recibido por parámetro, ejecuta el cambio de estado en la base
     * de datos y reorienta la navegación al detalle del usuario.
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

        boolean bloqueado = usuarioDao.cambiarEstadoUsuario(
                idUsuario,
                "INACTIVA"
        );

        if (!bloqueado) {
            System.err.println("Error al bloquear usuario: " + idUsuario);
        } else {
            System.out.println("Usuario " + idUsuario + " bloqueado correctamente.");
        }

        resp.sendRedirect(
                "detalle-usuario-admin?idUsuario=" + idUsuario
        );
    }
}
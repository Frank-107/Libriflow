package mx.edu.utez.libriflow.controller;

import mx.edu.utez.libriflow.model.Dao.UsuarioDao;
import mx.edu.utez.libriflow.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controlador Servlet encargado de la administración del catálogo de usuarios del sistema.
 * Permite a la capa de administración obtener el listado completo de usuarios registrados
 * y despacharlo hacia la vista de gestión administrativa correspondiente.
 *
 * @author Monserrath
 * @since 24/08/2026
 */

@WebServlet(name = "UsuariosAdminSv", value = "/usuarios-admin")
public class UsuariosAdminSv extends HttpServlet {

    /**
     * Procesa la petición HTTP GET para obtener la lista general de usuarios.
     * Instancia el objeto de acceso a datos de usuarios, ejecuta la consulta global de registros
     * y transfiere la lista como atributo hacia la plantilla JSP {@code /UsuariosAdmin.jsp}.
     *
     * @param request Objeto HttpServletRequest que transporta los atributos y la solicitud HTTP.
     * @param response Objeto HttpServletResponse para gestionar el despacho o reenvío.
     * @throws ServletException Si ocurre una excepción interna en el contenedor de Servlets.
     * @throws IOException Si ocurre un error de entrada/salida durante el reenvío de la petición.
     *
     * @author Monserrath
     * @since 24/08/2026
     */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UsuarioDao usuarioDao = new UsuarioDao();
        List<Usuario> listaUsuarios = usuarioDao.getAll();

        request.setAttribute("usuarios", listaUsuarios);
        request.getRequestDispatcher("/UsuariosAdmin.jsp").forward(request, response);
    }

    /**
     * Procesa las peticiones HTTP POST reorientándolas hacia el método {@link #doGet}.
     * Permite canalizar peticiones enviadas por formulario hacia la misma vista de administración.
     *
     * @param request Objeto HttpServletRequest con la información de la solicitud.
     * @param response Objeto HttpServletResponse para la respuesta HTTP.
     * @throws ServletException Si ocurre un error en la ejecución del Servlet.
     * @throws IOException Si ocurre un error de lectura o escritura HTTP.
     *
     * @author Monserrath
     * @since 24/08/2026
     */

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
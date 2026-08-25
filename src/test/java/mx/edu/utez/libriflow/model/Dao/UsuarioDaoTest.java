package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Usuario;
import mx.edu.utez.libriflow.utils.SQLconnector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDaoTest {

    private UsuarioDao usuarioDao;

    private final List<Integer> usuariosCreados = new ArrayList<>();

    @BeforeEach
    void setUp() {
        usuarioDao = new UsuarioDao();
    }

    @AfterEach
    void tearDown() {

        String sql = "DELETE FROM Usuario WHERE Id_Usuario = ?";

        for (Integer idUsuario : usuariosCreados) {

            try (Connection con = SQLconnector.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, idUsuario);
                ps.executeUpdate();

            } catch (SQLException e) {
                System.out.println("No se pudo eliminar el usuario de prueba: " + idUsuario);
                e.printStackTrace();
            }
        }

        usuariosCreados.clear();
    }

    private Usuario crearUsuarioPrueba() {

        Usuario usuario = new Usuario();

        usuario.setNombre("Usuario");
        usuario.setApellidoPaterno("Prueba");
        usuario.setApellidoMaterno("JUnit");

        String correo =
                "junit" + System.nanoTime() + "@test.com";

        usuario.setCorreo(correo);
        usuario.setTelefono("7771234567");

        int idUsuario = usuarioDao.create(usuario);

        assertTrue(
                idUsuario > 0,
                "El usuario de prueba debe crearse correctamente"
        );

        usuariosCreados.add(idUsuario);

        usuario.setId(idUsuario);

        return usuario;
    }

    @Test
    void create() {

        Usuario usuario = new Usuario();

        usuario.setNombre("Andres");
        usuario.setApellidoPaterno("Angelina");
        usuario.setApellidoMaterno("Perez");

        String correo =
                "create" + System.nanoTime() + "@test.com";

        usuario.setCorreo(correo);
        usuario.setTelefono("7771112233");

        int idUsuario = usuarioDao.create(usuario);

        assertTrue(
                idUsuario > 0,
                "El método create debe regresar un ID válido"
        );

        usuariosCreados.add(idUsuario);

        assertTrue(
                usuarioDao.correoExistente(correo),
                "El usuario creado debe existir en la base de datos"
        );
    }

    @Test
    void correoExistente() {

        Usuario usuario = crearUsuarioPrueba();

        boolean existe =
                usuarioDao.correoExistente(usuario.getCorreo());

        assertTrue(
                existe,
                "El correo registrado debe existir"
        );

        boolean correoInexistente =
                usuarioDao.correoExistente(
                        "noexiste" + System.nanoTime() + "@test.com"
                );

        assertFalse(
                correoInexistente,
                "Un correo no registrado no debe existir"
        );
    }

    @Test
    void obtenerUsuario() {

        Usuario usuarioCreado = crearUsuarioPrueba();

        Usuario usuarioObtenido =
                usuarioDao.obtenerUsuario(
                        usuarioCreado.getCorreo()
                );

        assertNotNull(
                usuarioObtenido,
                "El usuario obtenido no debe ser null"
        );

        assertEquals(
                usuarioCreado.getId(),
                usuarioObtenido.getId()
        );

        assertEquals(
                usuarioCreado.getNombre(),
                usuarioObtenido.getNombre()
        );

        assertEquals(
                usuarioCreado.getCorreo(),
                usuarioObtenido.getCorreo()
        );

        assertEquals(
                "ACTIVA",
                usuarioObtenido.getEstado()
        );
    }

    @Test
    void getIdUsuario() {

        Usuario usuario = crearUsuarioPrueba();

        int idObtenido =
                usuarioDao.getIdUsuario(
                        usuario.getCorreo()
                );

        assertEquals(
                usuario.getId(),
                idObtenido,
                "El ID obtenido debe ser igual al ID del usuario creado"
        );
    }

    @Test
    void getAll() {

        Usuario usuario = crearUsuarioPrueba();

        List<Usuario> usuarios =
                usuarioDao.getAll();

        assertNotNull(
                usuarios,
                "La lista de usuarios no debe ser null"
        );

        assertFalse(
                usuarios.isEmpty(),
                "La lista de usuarios no debe estar vacía"
        );

        boolean usuarioEncontrado =
                usuarios.stream()
                        .anyMatch(
                                u -> u.getId() == usuario.getId()
                        );

        assertTrue(
                usuarioEncontrado,
                "El usuario creado debe aparecer en la lista"
        );
    }

    @Test
    void getById() {

        Usuario usuarioCreado = crearUsuarioPrueba();

        Usuario usuarioObtenido =
                usuarioDao.getById(
                        usuarioCreado.getId()
                );

        assertNotNull(
                usuarioObtenido,
                "El usuario obtenido no debe ser null"
        );

        assertEquals(
                usuarioCreado.getId(),
                usuarioObtenido.getId()
        );

        assertEquals(
                usuarioCreado.getCorreo(),
                usuarioObtenido.getCorreo()
        );

        assertEquals(
                usuarioCreado.getNombre(),
                usuarioObtenido.getNombre()
        );
    }

    @Test
    void update() {

        Usuario usuario = crearUsuarioPrueba();

        usuario.setNombre("Usuario Actualizado");
        usuario.setApellidoPaterno("Apellido Actualizado");
        usuario.setApellidoMaterno("JUnit");
        usuario.setTelefono("7779998877");

        boolean resultado =
                usuarioDao.update(usuario);

        assertTrue(
                resultado,
                "El usuario debe actualizarse correctamente"
        );

        Usuario usuarioActualizado =
                usuarioDao.getById(
                        usuario.getId()
                );

        assertEquals(
                "Usuario Actualizado",
                usuarioActualizado.getNombre()
        );

        assertEquals(
                "Apellido Actualizado",
                usuarioActualizado.getApellidoPaterno()
        );

        assertEquals(
                "7779998877",
                usuarioActualizado.getTelefono()
        );
    }

    @Test
    void actualizarContrasena() {

        Usuario usuario = crearUsuarioPrueba();

        boolean resultado =
                usuarioDao.actualizarContrasena(
                        usuario.getCorreo(),
                        "NuevaContrasena123"
                );

        /*
         * El usuario creado directamente mediante UsuarioDao
         * todavía no tiene una credencial asociada.
         *
         * La prueba completa del cambio de contraseña se realizará
         * en CredencialDaoTest.
         */
        assertFalse(
                resultado,
                "Sin una credencial asociada no debe actualizarse ninguna contraseña"
        );
    }

    @Test
    void getDuenoPublicacionById() {

        Usuario usuario =
                usuarioDao.getDuenoPublicacionById(-1);

        assertNull(
                usuario,
                "Una publicación inexistente no debe tener propietario"
        );
    }

    @Test
    void activarUsuario() {

        Usuario usuario = crearUsuarioPrueba();

        boolean cambioEstado =
                usuarioDao.cambiarEstadoUsuario(
                        usuario.getId(),
                        "INACTIVA"
                );

        assertTrue(cambioEstado);

        Usuario usuarioInactivo =
                usuarioDao.getById(
                        usuario.getId()
                );

        assertEquals(
                "INACTIVA",
                usuarioInactivo.getEstado()
        );

        boolean resultado =
                usuarioDao.activarUsuario(
                        usuario.getId()
                );

        assertTrue(
                resultado,
                "El usuario debe poder activarse"
        );

        Usuario usuarioActivo =
                usuarioDao.getById(
                        usuario.getId()
                );

        assertEquals(
                "ACTIVA",
                usuarioActivo.getEstado()
        );
    }

    @Test
    void cambiarEstadoUsuario() {

        Usuario usuario = crearUsuarioPrueba();

        boolean resultado =
                usuarioDao.cambiarEstadoUsuario(
                        usuario.getId(),
                        "INACTIVA"
                );

        assertTrue(
                resultado,
                "El estado del usuario debe actualizarse"
        );

        Usuario usuarioActualizado =
                usuarioDao.getById(
                        usuario.getId()
                );

        assertEquals(
                "INACTIVA",
                usuarioActualizado.getEstado()
        );
    }

    @Test
    void delete() {

        /*
         * Actualmente el método delete de UsuarioDao
         * no está implementado y siempre devuelve false.
         */

        boolean resultado =
                usuarioDao.delete(-1);

        assertFalse(
                resultado,
                "Actualmente delete debe devolver false"
        );
    }
}
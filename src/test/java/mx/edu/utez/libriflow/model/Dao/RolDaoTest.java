package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Usuario;
import mx.edu.utez.libriflow.utils.SQLconnector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class RolDaoTest {

    private RolDao rolDao;
    private UsuarioDao usuarioDao;

    private int idUsuarioPrueba = -1;

    @BeforeEach
    void setUp() {
        rolDao = new RolDao();
        usuarioDao = new UsuarioDao();
    }

    @AfterEach
    void tearDown() {

        if (idUsuarioPrueba == -1) {
            return;
        }

        try (Connection con = SQLconnector.getConnection()) {

            String sqlRol =
                    "DELETE FROM Rol WHERE id_usuario = ?";

            try (PreparedStatement ps =
                         con.prepareStatement(sqlRol)) {

                ps.setInt(1, idUsuarioPrueba);
                ps.executeUpdate();
            }

            String sqlUsuario =
                    "DELETE FROM Usuario WHERE id_usuario = ?";

            try (PreparedStatement ps =
                         con.prepareStatement(sqlUsuario)) {

                ps.setInt(1, idUsuarioPrueba);
                ps.executeUpdate();
            }

        } catch (SQLException e) {

            System.out.println(
                    "No se pudieron eliminar los datos de prueba."
            );

            e.printStackTrace();
        }

        idUsuarioPrueba = -1;
    }

    private Usuario crearUsuarioPrueba() {

        Usuario usuario = new Usuario();

        usuario.setNombre("Usuario");
        usuario.setApellidoPaterno("Rol");
        usuario.setApellidoMaterno("JUnit");

        usuario.setCorreo(
                "rol" +
                        System.nanoTime() +
                        "@test.com"
        );

        usuario.setTelefono("7771234567");

        idUsuarioPrueba =
                usuarioDao.create(usuario);

        assertTrue(
                idUsuarioPrueba > 0,
                "El usuario de prueba debe crearse correctamente"
        );

        usuario.setId(idUsuarioPrueba);

        return usuario;
    }

    @Test
    void create() {

        Usuario usuario =
                crearUsuarioPrueba();

        boolean resultado =
                rolDao.create(
                        usuario.getId()
                );

        assertTrue(
                resultado,
                "El rol debe asignarse correctamente"
        );

        String rol =
                rolDao.obtenerRol(
                        usuario.getId()
                );

        assertEquals(
                "USUARIO",
                rol,
                "El rol asignado por defecto debe ser USUARIO"
        );
    }

    @Test
    void obtenerRol() {

        Usuario usuario =
                crearUsuarioPrueba();

        boolean creado =
                rolDao.create(
                        usuario.getId()
                );

        assertTrue(
                creado,
                "El rol inicial debe crearse correctamente"
        );

        String rolInicial =
                rolDao.obtenerRol(
                        usuario.getId()
                );

        assertEquals(
                "USUARIO",
                rolInicial,
                "El rol inicial debe ser USUARIO"
        );

        cambiarRolAdmin(
                usuario.getId()
        );

        String rolActualizado =
                rolDao.obtenerRol(
                        usuario.getId()
                );

        assertEquals(
                "ADMIN",
                rolActualizado,
                "El método obtenerRol debe recuperar el rol ADMIN almacenado en la base de datos"
        );
    }

    private void cambiarRolAdmin(int idUsuario) {

        String sql =
                "UPDATE Rol SET rol = 'ADMIN' WHERE id_usuario = ?";

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            int filasAfectadas =
                    ps.executeUpdate();

            assertTrue(
                    filasAfectadas > 0,
                    "El rol debe poder modificarse para realizar la prueba"
            );

        } catch (SQLException e) {

            fail(
                    "Error al preparar el rol ADMIN para la prueba: "
                            + e.getMessage()
            );
        }
    }
}
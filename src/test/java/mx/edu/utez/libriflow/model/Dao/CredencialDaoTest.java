package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Usuario;
import mx.edu.utez.libriflow.utils.SQLconnector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class CredencialDaoTest {

    private CredencialDao credencialDao;
    private UsuarioDao usuarioDao;

    private int idUsuarioPrueba = -1;

    @BeforeEach
    void setUp() {
        credencialDao = new CredencialDao();
        usuarioDao = new UsuarioDao();
    }

    @AfterEach
    void tearDown() {

        if (idUsuarioPrueba == -1) {
            return;
        }

        try (Connection con = SQLconnector.getConnection()) {

            String sqlCredencial =
                    "DELETE FROM Credencial WHERE id_usuario = ?";

            try (PreparedStatement ps =
                         con.prepareStatement(sqlCredencial)) {

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
        usuario.setApellidoPaterno("Credencial");
        usuario.setApellidoMaterno("JUnit");

        usuario.setCorreo(
                "credencial" +
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


    private String generarHash(String contrasena) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            contrasena.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            StringBuilder hexadecimal =
                    new StringBuilder();

            for (byte b : hash) {

                hexadecimal.append(
                        String.format("%02X", b)
                );
            }

            return hexadecimal.toString();

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(e);
        }
    }


    @Test
    void create() {

        Usuario usuario =
                crearUsuarioPrueba();

        String contrasena =
                "Prueba123";

        String hash =
                generarHash(contrasena);

        boolean resultado =
                credencialDao.create(
                        hash,
                        usuario.getId()
                );

        assertTrue(
                resultado,
                "La credencial debe registrarse correctamente"
        );

        assertTrue(
                credencialDao.validarContrasena(
                        usuario.getId(),
                        contrasena
                ),
                "La contraseña registrada debe ser válida"
        );
    }


    @Test
    void validarContrasena() {

        Usuario usuario =
                crearUsuarioPrueba();

        String contrasenaCorrecta =
                "Password123";

        String hash =
                generarHash(contrasenaCorrecta);

        boolean creada =
                credencialDao.create(
                        hash,
                        usuario.getId()
                );

        assertTrue(
                creada,
                "La credencial debe crearse antes de realizar la validación"
        );


        boolean correcta =
                credencialDao.validarContrasena(
                        usuario.getId(),
                        contrasenaCorrecta
                );

        assertTrue(
                correcta,
                "La contraseña correcta debe ser aceptada"
        );


        boolean incorrecta =
                credencialDao.validarContrasena(
                        usuario.getId(),
                        "PasswordIncorrecto"
                );

        assertFalse(
                incorrecta,
                "Una contraseña incorrecta debe ser rechazada"
        );
    }


    @Test
    void getAll() {

        /*
         * Actualmente el método getAll
         * todavía no está implementado
         * y devuelve null.
         */

        assertNull(
                credencialDao.getAll(),
                "Actualmente getAll debe devolver null"
        );
    }


    @Test
    void getById() {

        /*
         * Actualmente el método getById
         * todavía no está implementado
         * y devuelve null.
         */

        assertNull(
                credencialDao.getById(1),
                "Actualmente getById debe devolver null"
        );
    }


    @Test
    void updateCredencial() {

        Usuario usuario =
                crearUsuarioPrueba();

        String contrasenaInicial =
                "Inicial123";

        String hashInicial =
                generarHash(contrasenaInicial);

        boolean creada =
                credencialDao.create(
                        hashInicial,
                        usuario.getId()
                );

        assertTrue(
                creada,
                "La credencial inicial debe crearse correctamente"
        );


        String nuevaContrasena =
                "NuevaPassword456";

        String nuevoHash =
                generarHash(nuevaContrasena);

        usuario.setContrasenaHash(
                nuevoHash
        );


        boolean actualizada =
                credencialDao.updateCredencial(
                        usuario
                );

        assertTrue(
                actualizada,
                "La credencial debe actualizarse correctamente"
        );


        assertTrue(
                credencialDao.validarContrasena(
                        usuario.getId(),
                        nuevaContrasena
                ),
                "La nueva contraseña debe ser válida"
        );


        assertFalse(
                credencialDao.validarContrasena(
                        usuario.getId(),
                        contrasenaInicial
                ),
                "La contraseña anterior ya no debe ser válida"
        );
    }


    @Test
    void delete() {

        /*
         * Actualmente el método delete
         * no está implementado y siempre
         * devuelve false.
         */

        boolean resultado =
                credencialDao.delete(1);

        assertFalse(
                resultado,
                "Actualmente delete debe devolver false"
        );
    }
}
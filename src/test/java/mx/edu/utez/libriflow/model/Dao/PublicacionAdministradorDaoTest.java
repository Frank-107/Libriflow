package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.PublicacionAdminCompleta;
import mx.edu.utez.libriflow.model.PublicacionAdministrador;
import mx.edu.utez.libriflow.model.PublicacionResumen;
import mx.edu.utez.libriflow.testconfig.OracleTestBase;
import mx.edu.utez.libriflow.utils.SQLconnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para {@link PublicacionAdministradorDao} utilizando
 * Oracle ejecutado temporalmente mediante Docker y Testcontainers.
 *
 * Cada prueba genera los datos que necesita para evitar dependencias
 * con información preexistente de la base de datos de producción.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 25/08/2026
 */
class PublicacionAdministradorDaoTest extends OracleTestBase {

    private PublicacionAdministradorDao dao;

    /**
     * Inicializa el DAO antes de ejecutar cada prueba.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @BeforeEach
    void setUp() {
        dao = new PublicacionAdministradorDao();
    }

    /**
     * Comprueba que una publicación oficial pueda registrarse correctamente.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void create() {

        int idLibro = crearLibroPrueba("Libro Admin Create");

        PublicacionAdministrador entidad =
                crearEntidadAdministrador(idLibro);

        int idPublicacion = dao.create(entidad);

        assertTrue(
                idPublicacion > 0,
                "create debe devolver un ID válido"
        );

        PublicacionAdministrador guardada =
                dao.getById(idPublicacion);

        assertNotNull(guardada);
        assertEquals(idLibro, guardada.getIdLibro());
        assertEquals(5, guardada.getCantidad());
        assertEquals(1, guardada.getEsVenta());
        assertEquals(0, guardada.getEsRenta());
        assertEquals(299.99, guardada.getPrecio(), 0.001);
        assertEquals("ACTIVO", guardada.getEstado());
    }

    /**
     * Comprueba que se obtengan las publicaciones oficiales registradas.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getAll() {

        int idLibro1 =
                crearLibroPrueba("Libro Admin Lista 1");

        int idLibro2 =
                crearLibroPrueba("Libro Admin Lista 2");

        int idPublicacion1 =
                dao.create(
                        crearEntidadAdministrador(idLibro1)
                );

        int idPublicacion2 =
                dao.create(
                        crearEntidadAdministrador(idLibro2)
                );

        assertTrue(idPublicacion1 > 0);
        assertTrue(idPublicacion2 > 0);

        List<PublicacionAdministrador> publicaciones =
                dao.getAll();

        assertNotNull(publicaciones);

        assertTrue(
                publicaciones.stream()
                        .anyMatch(
                                p ->
                                        p.getIdPublicacionLf()
                                                == idPublicacion1
                        )
        );

        assertTrue(
                publicaciones.stream()
                        .anyMatch(
                                p ->
                                        p.getIdPublicacionLf()
                                                == idPublicacion2
                        )
        );
    }

    /**
     * Comprueba que una publicación pueda consultarse mediante su ID.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getById() {

        int idLibro =
                crearLibroPrueba("Libro Admin GetById");

        int idPublicacion =
                dao.create(
                        crearEntidadAdministrador(idLibro)
                );

        PublicacionAdministrador resultado =
                dao.getById(idPublicacion);

        assertNotNull(resultado);
        assertEquals(
                idPublicacion,
                resultado.getIdPublicacionLf()
        );
        assertEquals(
                idLibro,
                resultado.getIdLibro()
        );
        assertEquals(
                "Publicación oficial para pruebas Docker.",
                resultado.getSinopsis()
        );
    }

    /**
     * Comprueba que consultar un ID inexistente devuelva {@code null}.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getByIdInexistente() {

        assertNull(
                dao.getById(-999)
        );
    }

    /**
     * Comprueba la actualización de los datos principales de una publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void update() {

        int idLibro =
                crearLibroPrueba("Libro Admin Update");

        int idPublicacion =
                dao.create(
                        crearEntidadAdministrador(idLibro)
                );

        PublicacionAdministrador publicacion =
                dao.getById(idPublicacion);

        assertNotNull(publicacion);

        publicacion.setEstado("INACTIVO");
        publicacion.setCantidad(12);
        publicacion.setSinopsis("Sinopsis actualizada desde JUnit.");
        publicacion.setEsVenta(0);
        publicacion.setEsRenta(1);
        publicacion.setPrecio(450.50);

        boolean actualizado =
                dao.update(publicacion);

        assertTrue(actualizado);

        PublicacionAdministrador resultado =
                dao.getById(idPublicacion);

        assertNotNull(resultado);
        assertEquals("INACTIVO", resultado.getEstado());
        assertEquals(12, resultado.getCantidad());
        assertEquals(
                "Sinopsis actualizada desde JUnit.",
                resultado.getSinopsis()
        );
        assertEquals(0, resultado.getEsVenta());
        assertEquals(1, resultado.getEsRenta());
        assertEquals(450.50, resultado.getPrecio(), 0.001);
    }

    /**
     * Comprueba la eliminación física de una publicación y sus imágenes.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void delete() {

        int idLibro =
                crearLibroPrueba("Libro Admin Delete");

        int idPublicacion =
                dao.create(
                        crearEntidadAdministrador(idLibro)
                );

        crearImagenAdministrador(
                idPublicacion,
                1,
                "admin-delete.jpg"
        );

        boolean eliminado =
                dao.delete(idPublicacion);

        assertTrue(eliminado);
        assertNull(
                dao.getById(idPublicacion)
        );

        assertEquals(
                0,
                contarImagenesAdministrador(idPublicacion)
        );
    }

    /**
     * Comprueba que eliminar una publicación inexistente devuelva {@code false}.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void deleteInexistente() {

        assertFalse(
                dao.delete(-999)
        );
    }

    /**
     * Comprueba que el catálogo devuelva una publicación activa
     * cuando tiene una imagen principal asociada.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getResumenCatalogo() {

        int idLibro =
                crearLibroPrueba("Libro Admin Catalogo");

        int idPublicacion =
                dao.create(
                        crearEntidadAdministrador(idLibro)
                );

        crearImagenAdministrador(
                idPublicacion,
                1,
                "admin-catalogo.jpg"
        );

        List<PublicacionResumen> catalogo =
                dao.getResumenCatalogo();

        assertNotNull(catalogo);

        PublicacionResumen encontrado =
                catalogo.stream()
                        .filter(
                                p ->
                                        p.getIdPublicacion()
                                                == idPublicacion
                        )
                        .findFirst()
                        .orElse(null);

        assertNotNull(
                encontrado,
                "La publicación activa debe aparecer en el catálogo"
        );

        assertEquals(
                "Libro Admin Catalogo",
                encontrado.getTitulo()
        );

        assertEquals(
                "admin-catalogo.jpg",
                encontrado.getImagenPrincipal()
        );

    }

    /**
     * Comprueba la obtención del detalle completo de una publicación oficial.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void getPublicacionAdminCompleta() {

        int idLibro =
                crearLibroPrueba("Libro Admin Completo");

        int idPublicacion =
                dao.create(
                        crearEntidadAdministrador(idLibro)
                );

        crearImagenAdministrador(
                idPublicacion,
                1,
                "principal-admin.jpg"
        );

        crearImagenAdministrador(
                idPublicacion,
                2,
                "reverso-admin.jpg"
        );

        crearImagenAdministrador(
                idPublicacion,
                3,
                "interior-admin.jpg"
        );

        PublicacionAdminCompleta detalle =
                dao.getPublicacionAdminCompleta(
                        idPublicacion
                );

        assertNotNull(detalle);

        assertEquals(
                idPublicacion,
                detalle.getIdPublicacionLf()
        );

        assertEquals(
                idLibro,
                detalle.getIdLibro()
        );

        assertEquals(
                "Libro Admin Completo",
                detalle.getTitulo()
        );

        assertEquals(
                "Publicación oficial para pruebas Docker.",
                detalle.getSinopsis()
        );

        assertEquals(
                "principal-admin.jpg",
                detalle.getImagenPrincipal()
        );

        assertEquals(
                "reverso-admin.jpg",
                detalle.getImagenReverso()
        );

        assertEquals(
                "interior-admin.jpg",
                detalle.getImagenInterior()
        );
    }

    /**
     * Comprueba que el inventario disminuya en una unidad.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void disminuirInventario() {

        int idLibro =
                crearLibroPrueba("Libro Admin Inventario");

        int idPublicacion =
                dao.create(
                        crearEntidadAdministrador(idLibro)
                );

        PublicacionAdministrador antes =
                dao.getById(idPublicacion);

        assertNotNull(antes);
        assertEquals(5, antes.getCantidad());

        boolean disminuido =
                dao.disminuirInventario(
                        idPublicacion
                );

        assertTrue(disminuido);

        PublicacionAdministrador despues =
                dao.getById(idPublicacion);

        assertNotNull(despues);
        assertEquals(4, despues.getCantidad());
    }

    /**
     * Comprueba que no pueda disminuirse un inventario que ya se encuentra en cero.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void disminuirInventarioSinExistencias() {

        int idLibro =
                crearLibroPrueba("Libro Admin Sin Stock");

        PublicacionAdministrador entidad =
                crearEntidadAdministrador(idLibro);

        entidad.setCantidad(0);

        int idPublicacion =
                dao.create(entidad);

        assertFalse(
                dao.disminuirInventario(
                        idPublicacion
                )
        );

        assertEquals(
                0,
                dao.getById(idPublicacion)
                        .getCantidad()
        );
    }

    /**
     * Comprueba la baja lógica de una publicación oficial.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    @Test
    void darDeBajaPublicacionAdmin() {

        int idLibro =
                crearLibroPrueba("Libro Admin Baja");

        int idPublicacion =
                dao.create(
                        crearEntidadAdministrador(idLibro)
                );

        boolean dadoDeBaja =
                dao.darDeBajaPublicacionAdmin(
                        idPublicacion
                );

        assertTrue(dadoDeBaja);

        PublicacionAdministrador resultado =
                dao.getById(idPublicacion);

        assertNotNull(resultado);
        assertEquals(
                "INACTIVO",
                resultado.getEstado()
        );
    }

    /**
     * Crea una entidad de publicación oficial con valores válidos
     * reutilizables por las diferentes pruebas.
     *
     * @param idLibro Identificador del libro relacionado.
     * @return Entidad preparada para ser registrada.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private PublicacionAdministrador crearEntidadAdministrador(
            int idLibro) {

        PublicacionAdministrador entidad =
                new PublicacionAdministrador();

        entidad.setIdLibro(idLibro);
        entidad.setSinopsis(
                "Publicación oficial para pruebas Docker."
        );
        entidad.setCantidad(5);
        entidad.setEsVenta(1);
        entidad.setEsRenta(0);
        entidad.setPrecio(299.99);

        return entidad;
    }

    /**
     * Crea un libro auxiliar necesario para satisfacer la llave foránea
     * de una publicación oficial.
     *
     * @param titulo Título que tendrá el libro de prueba.
     * @return Identificador generado del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int crearLibroPrueba(
            String titulo) {

        String sql = """
                INSERT INTO libro(
                    titulo,
                    autor,
                    editorial,
                    genero
                )
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_LIBRO"}
             )) {

            ps.setString(1, titulo);
            ps.setString(2, "Autor Admin Test");
            ps.setString(3, "Editorial Admin Test");
            ps.setString(4, "Tecnología");

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {
                throw new SQLException(
                        "No se pudo crear el libro de prueba"
                );
            }

            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo crear el libro de prueba",
                    e
            );
        }

        throw new IllegalStateException(
                "No se obtuvo el ID del libro de prueba"
        );
    }

    /**
     * Crea una imagen relacionada con una publicación oficial.
     *
     * @param idPublicacion Identificador de la publicación oficial.
     * @param tipo Tipo de imagen a registrar.
     * @param nombre Nombre o ruta simulada de la imagen.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private void crearImagenAdministrador(
            int idPublicacion,
            int tipo,
            String nombre) {

        String sql = """
                INSERT INTO imagen(
                    id_publicacion_lf,
                    imagen,
                    tipo
                )
                VALUES (?, ?, ?)
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idPublicacion
            );

            ps.setString(
                    2,
                    nombre
            );

            ps.setInt(
                    3,
                    tipo
            );

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo crear la imagen de prueba",
                    e
            );
        }
    }

    /**
     * Cuenta las imágenes relacionadas con una publicación oficial.
     *
     * @param idPublicacion Identificador de la publicación.
     * @return Cantidad de imágenes relacionadas.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 25/08/2026
     */
    private int contarImagenesAdministrador(
            int idPublicacion) {

        String sql = """
                SELECT COUNT(*)
                FROM imagen
                WHERE id_publicacion_lf = ?
                """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idPublicacion
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudieron contar las imágenes",
                    e
            );
        }

        return 0;
    }
}
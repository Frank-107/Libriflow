package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Libro;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para {@link LibroDao} ejecutadas directamente sobre la BD de Oracle.
 *
 * @author Santi
 * @since 25/08/2026
 */
class LibroDaoTest {

    private LibroDao libroDao;

    @BeforeAll
    static void initConfig() {
        // Establecemos las propiedades del sistema que busca SQLconnector / credentials.properties
        // NOTA: Reemplaza con los valores reales de tu BD de Oracle en la nube
        System.setProperty("DB_URL", "jdbc:oracle:thin:@tu_oracle_db_url");
        System.setProperty("DB_USER", "tu_usuario");
        System.setProperty("DB_PASS", "tu_contraseña");
    }

    @BeforeEach
    void setUp() {
        libroDao = new LibroDao();
    }

    @AfterEach
    void tearDown() {
        libroDao = null;
    }

    @Test
    @DisplayName("Prueba de Create: Inserta un libro en la base de datos")
    void create() {
        Libro libro = new Libro();
        libro.setTitulo("Cien Años de Soledad");
        libro.setAutor("Gabriel García Márquez");
        libro.setEditorial("Editorial Sudamericana");
        libro.setGenero("Novela");

        int idGenerado = libroDao.create(libro);

        // Verifica que la BD devuelva un ID válido (mayor a 0)
        assertTrue(idGenerado > 0, "El ID del libro insertado debe ser mayor a 0");
    }

    @Test
    @DisplayName("Prueba de GetAll: Consulta la lista de libros")
    void getAll() {
        List<Libro> lista = libroDao.getAll();

        // El DAO actual retorna null en esta etapa de desarrollo
        assertNull(lista, "Debe retornar null hasta que se implemente la consulta completa");
    }

    @Test
    @DisplayName("Prueba de GetById: Consulta un libro por su ID")
    void getById() {
        Libro libro = libroDao.getById(1);

        // El DAO actual retorna null en esta etapa de desarrollo
        assertNull(libro, "Debe retornar null hasta que se implemente la consulta completa");
    }

    @Test
    @DisplayName("Prueba de Update: Actualiza un libro existente")
    void update() {
        Libro libro = new Libro();
        boolean resultado = libroDao.update(libro);

        // El DAO actual retorna false en esta etapa de desarrollo
        assertFalse(resultado, "Debe retornar false hasta que se implemente la actualización");
    }

    @Test
    @DisplayName("Prueba de Delete: Elimina un libro por su ID")
    void delete() {
        boolean resultado = libroDao.delete(1);

        // El DAO actual retorna false en esta etapa de desarrollo
        assertFalse(resultado, "Debe retornar false hasta que se implemente la eliminación");
    }
}
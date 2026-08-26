package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Libro;
import mx.edu.utez.libriflow.testconfig.OracleTestBase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibroDaoTest extends OracleTestBase {

    private LibroDao libroDao;

    @BeforeEach
    void setUp() {
        libroDao = new LibroDao();
    }


    private Libro crearLibroPrueba() {

        Libro libro = new Libro();

        libro.setTitulo(
                "Libro JUnit " + System.nanoTime()
        );

        libro.setAutor(
                "Autor Prueba"
        );

        libro.setEditorial(
                "Editorial Prueba"
        );

        libro.setGenero(
                "Tecnología"
        );

        int idLibro =
                libroDao.create(libro);

        assertTrue(
                idLibro > 0,
                "El libro debe crearse correctamente"
        );

        libro.setIdLibro(idLibro);

        return libro;
    }


    @Test
    void create() {

        Libro libro = new Libro();

        libro.setTitulo(
                "Java con JUnit"
        );

        libro.setAutor(
                "Autor Test"
        );

        libro.setEditorial(
                "Editorial Test"
        );

        libro.setGenero(
                "Tecnología"
        );

        int idLibro =
                libroDao.create(libro);

        assertTrue(
                idLibro > 0,
                "create debe devolver un ID válido"
        );


        Libro libroGuardado =
                libroDao.getById(idLibro);

        assertNotNull(
                libroGuardado
        );

        assertEquals(
                "Java con JUnit",
                libroGuardado.getTitulo()
        );
    }


    @Test
    void getAll() {

        Libro libro1 =
                crearLibroPrueba();

        Libro libro2 =
                crearLibroPrueba();


        List<Libro> libros =
                libroDao.getAll();


        assertNotNull(
                libros,
                "La lista no debe ser null"
        );

        assertTrue(
                libros.size() >= 2,
                "Deben encontrarse los libros creados"
        );


        boolean primeroEncontrado =
                libros.stream()
                        .anyMatch(
                                libro ->
                                        libro.getIdLibro()
                                                == libro1.getIdLibro()
                        );

        boolean segundoEncontrado =
                libros.stream()
                        .anyMatch(
                                libro ->
                                        libro.getIdLibro()
                                                == libro2.getIdLibro()
                        );


        assertTrue(
                primeroEncontrado
        );

        assertTrue(
                segundoEncontrado
        );
    }


    @Test
    void getById() {

        Libro creado =
                crearLibroPrueba();


        Libro encontrado =
                libroDao.getById(
                        creado.getIdLibro()
                );


        assertNotNull(
                encontrado,
                "El libro debe encontrarse"
        );

        assertEquals(
                creado.getIdLibro(),
                encontrado.getIdLibro()
        );

        assertEquals(
                creado.getTitulo(),
                encontrado.getTitulo()
        );

        assertEquals(
                creado.getAutor(),
                encontrado.getAutor()
        );
    }


    @Test
    void getByIdInexistente() {

        Libro resultado =
                libroDao.getById(-999);


        assertNull(
                resultado,
                "Un ID inexistente debe devolver null"
        );
    }


    @Test
    void update() {

        Libro libro =
                crearLibroPrueba();


        libro.setTitulo(
                "Libro Actualizado"
        );

        libro.setAutor(
                "Autor Actualizado"
        );

        libro.setEditorial(
                "Editorial Actualizada"
        );

        libro.setGenero(
                "Ciencia"
        );


        boolean resultado =
                libroDao.update(libro);


        assertTrue(
                resultado,
                "El libro debe actualizarse"
        );


        Libro actualizado =
                libroDao.getById(
                        libro.getIdLibro()
                );


        assertNotNull(
                actualizado
        );

        assertEquals(
                "Libro Actualizado",
                actualizado.getTitulo()
        );

        assertEquals(
                "Autor Actualizado",
                actualizado.getAutor()
        );

        assertEquals(
                "Editorial Actualizada",
                actualizado.getEditorial()
        );

        assertEquals(
                "Ciencia",
                actualizado.getGenero()
        );
    }


    @Test
    void delete() {

        Libro libro =
                crearLibroPrueba();


        boolean eliminado =
                libroDao.delete(
                        libro.getIdLibro()
                );


        assertTrue(
                eliminado,
                "El libro debe eliminarse correctamente"
        );


        Libro resultado =
                libroDao.getById(
                        libro.getIdLibro()
                );


        assertNull(
                resultado,
                "El libro eliminado ya no debe existir"
        );
    }


    @Test
    void deleteInexistente() {

        boolean resultado =
                libroDao.delete(-999);


        assertFalse(
                resultado,
                "No se debe poder eliminar un libro inexistente"
        );
    }
}
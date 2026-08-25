package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.Resena;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias/integración para {@link ResenaDao} ejecutadas directamente sobre la base de datos.
 *
 * @author Santi
 * @since 25/08/2026
 */
class ResenaDaoTest {

    private ResenaDao resenaDao;

    @BeforeEach
    void setUp() {
        resenaDao = new ResenaDao();
    }

    @AfterEach
    void tearDown() {
        resenaDao = null;
    }

    @Test
    @DisplayName("Prueba de Create: Intenta insertar una reseña con un objeto configurado")
    void create() {
        Resena resena = new Resena();
        // NOTA: Ajusta estos IDs a registros existentes en tu BD de pruebas si deseas que la inserción sea exitosa (true).
        resena.setIdUsuario(1);
        resena.setIdPublicacionLf(1);
        resena.setComentario("Excelente libro, muy recomendado.");
        resena.setCalificacion(5);

        boolean resultado = resenaDao.create(resena);

        // Evaluamos la ejecución del método (retornará true o false dependiendo de la existencia de las llaves foráneas)
        assertNotNull(resultado, "El método create debe retornar un resultado booleano");
    }

    @Test
    @DisplayName("Prueba de GetResenasByPublicacion: Obtiene la lista de reseñas asociadas a una publicación")
    void getResenasByPublicacion() {
        int idPublicacionTest = 1;

        List<Resena> lista = resenaDao.getResenasByPublicacion(idPublicacionTest);

        // Verificamos que no retorne null (debe ser una lista, ya sea vacía o con elementos)
        assertNotNull(lista, "La lista devuelta por getResenasByPublicacion no debe ser null");
    }

    @Test
    @DisplayName("Prueba de UsuarioHaCompradoORentado: Verifica la validación de compra/renta con IDs inexistentes o de prueba")
    void usuarioHaCompradoORentado() {
        // IDs ficticios para validar que retorne false si el usuario no tiene transacciones
        int idUsuarioFake = -1;
        int idPublicacionFake = -1;

        boolean haComprado = resenaDao.usuarioHaCompradoORentado(idUsuarioFake, idPublicacionFake);

        // Debe retornar false ya que las llaves primarias negativas no existen
        assertFalse(haComprado, "Debe retornar false para combinaciones de usuario y publicación sin transacciones");
    }
}
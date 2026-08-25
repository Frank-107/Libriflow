package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.CompraResumen;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para la clase {@link CompraDao}.
 * Realiza ejecuciones directas sobre la base de datos Oracle sin hacer uso de mocks.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @version 1.0
 * @since 25/08/2026
 */
class CompraDaoTest {

    private CompraDao compraDao;

    @BeforeEach
    void setUp() {
        compraDao = new CompraDao();
    }

    @AfterEach
    void tearDown() {
        compraDao = null;
    }

    @Test
    @DisplayName("Consulta de historial de compras por ID de usuario")
    void testGetResumenComprasPorUsuario() {
        int idUsuarioPrueba = 31;

        List<CompraResumen> resultado = compraDao.getResumenComprasPorUsuario(idUsuarioPrueba);

        assertNotNull(resultado, "La lista devuelta por la base de datos no debe ser nula.");

        if (!resultado.isEmpty()) {
            CompraResumen primerElemento = resultado.get(0);
            assertTrue(primerElemento.getIdDetalle() > 0, "El ID de detalle debe ser mayor a 0.");
            assertNotNull(primerElemento.getTitulo(), "El título de la publicación no debe ser nulo.");
        }
    }

    @Test
    @DisplayName("Conteo total de ventas completadas de un vendedor")
    void testContarVentasPorUsuario() {
        int idUsuarioPrueba = 31;

        int totalVentas = compraDao.contarVentasPorUsuario(idUsuarioPrueba);

        assertTrue(totalVentas >= 0, "El total de ventas devueltas debe ser mayor o igual a cero.");
    }
}
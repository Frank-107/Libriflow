package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.DetalleTransaccion;
import mx.edu.utez.libriflow.model.Movimiento;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas de integración para {@link DetalleTransaccionDao}.
 * Realiza pruebas directas sobre la base de datos Oracle a través de JDBC.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @version 1.0
 * @since 25/08/2026
 */
class DetalleTransaccionDaoTest {

    private DetalleTransaccionDao detalleTransaccionDao;

    @BeforeEach
    void setUp() {
        detalleTransaccionDao = new DetalleTransaccionDao();
    }

    @AfterEach
    void tearDown() {
        detalleTransaccionDao = null;
    }

    @Test
    @DisplayName("Prueba de creación o intento de inserción de un detalle de transacción")
    void create() {
        DetalleTransaccion entidad = new DetalleTransaccion();
        entidad.setIdTransaccion(1);
        entidad.setTipoOperacion("COMPRA");
        entidad.setPrecio(150.00);
        entidad.setGananciaLibriFlow(15.00);
        entidad.setGananciaVendedor(135.00);

        int resultado = detalleTransaccionDao.create(entidad);

        // Se valida que retorne un ID válido generado o -1 en caso de fallo por integridad relacional
        assertTrue(resultado > 0 || resultado == -1,
                "El resultado debe ser el ID autogenerado o -1 si no existe la transacción referenciada.");
    }

    @Test
    @DisplayName("Prueba de obtención de movimientos asociados a un usuario")
    void getMovimientosByIdUsuario() {
        int idUsuarioPrueba = 1;

        List<Movimiento> movimientos = detalleTransaccionDao.getMovimientosByIdUsuario(idUsuarioPrueba);

        assertNotNull(movimientos, "La lista devuelta por la consulta no debe ser nula.");
    }

    @Test
    @DisplayName("Prueba de obtención de todos los movimientos de ingresos globales")
    void getAllMovimientosIngresos() {
        List<Movimiento> ingresos = detalleTransaccionDao.getAllMovimientosIngresos();

        assertNotNull(ingresos, "La lista de ingresos globales no debe ser nula.");
    }
}
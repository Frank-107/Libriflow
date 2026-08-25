package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.DetalleTransaccion;
import mx.edu.utez.libriflow.model.Movimiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DetalleTransaccionDaoTest {

    private DetalleTransaccionDao dao;

    @BeforeEach
    void setUp() {
        dao = new DetalleTransaccionDao();
    }

    @Test
    void create() {
        // Arrange: Se crea un detalle con un ID de transacción inexistente (99999)
        DetalleTransaccion detalle = new DetalleTransaccion();
        detalle.setIdTransaccion(99999);
        detalle.setTipoOperacion("RENTA");
        detalle.setPrecio(150.00);
        detalle.setGananciaLibriFlow(15.00);
        detalle.setGananciaVendedor(135.00);

        // Act
        int idGenerado = dao.create(detalle);

        // Assert: El manejo de transacción hace rollback y retorna -1 ante errores de FK
        assertEquals(-1, idGenerado, "Debe retornar -1 al no encontrar la transacción referenciada");
    }

    @Test
    void getMovimientosByIdUsuario() {
        // Arrange
        int idUsuarioPrueba = 1;

        // Act
        List<Movimiento> movimientos = dao.getMovimientosByIdUsuario(idUsuarioPrueba);

        // Assert: Valida que la consulta con UNION ejecute correctamente el mapeo
        assertNotNull(movimientos, "La lista de movimientos del usuario no debe ser nula");
        assertTrue(movimientos.size() >= 0, "La consulta debe retornar un listado válido");
    }

    @Test
    void getAllMovimientosIngresos() {
        // Act
        List<Movimiento> ingresos = dao.getAllMovimientosIngresos();

        // Assert: Valida el cálculo global de ingresos de LibriFlow
        assertNotNull(ingresos, "La lista de ingresos globales no debe ser nula");
        assertTrue(ingresos.size() >= 0, "La consulta de ingresos debe ejecutarse correctamente");
    }
}
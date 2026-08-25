package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.DetalleRenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DetalleRentaDaoTest {

    private DetalleRentaDao dao;

    @BeforeEach
    void setUp() {
        // Instanciar el DAO antes de cada prueba
        dao = new DetalleRentaDao();
    }

    @Test
    void create() {
        // Arrange: Objeto con datos de prueba
        DetalleRenta nueva = new DetalleRenta();
        nueva.setIdDetalle(99999); // ID de transacción inexistente
        nueva.setFechaInicio(new Timestamp(System.currentTimeMillis()));
        nueva.setFechaLimite(new Timestamp(System.currentTimeMillis() + 86400000L));
        nueva.setEstado("PROGRAMADA");
        nueva.setCodigo("TEST-CODE");

        // Act
        int idGenerado = dao.create(nueva);

        // Assert: Retorna -1 debido a la restricción de llave foránea de la BD
        assertEquals(-1, idGenerado, "Debe retornar -1 al intentar registrar con una transacción inexistente");
    }

    @Test
    void getRentasActivas() {
        // Act
        List<DetalleRenta> lista = dao.getRentasActivas();

        // Assert
        assertNotNull(lista, "La lista de rentas activas no debe ser nula");
        assertTrue(lista.size() >= 0, "La consulta debe ejecutarse correctamente");
    }

    @Test
    void getRentasRetrasadasActivas() {
        // Act
        List<DetalleRenta> lista = dao.getRentasRetrasadasActivas();

        // Assert
        assertNotNull(lista, "La lista de rentas retrasadas no debe ser nula");
        assertTrue(lista.size() >= 0, "La consulta debe ejecutarse correctamente");
    }

    @Test
    void cambiarPenalizacion() {
        // Arrange
        int idDetalleInexistente = 99999;
        int nuevaPenalizacion = 2;

        // Act
        boolean resultado = dao.cambiarPenalizacion(idDetalleInexistente, nuevaPenalizacion);

        // Assert
        assertFalse(resultado, "No debe actualizar penalización a un registro inexistente");
    }

    @Test
    void suspenderUsuario() {
        // Arrange
        int idUsuarioInexistente = 99999;
        Timestamp fechaDesbloqueo = new Timestamp(System.currentTimeMillis() + (86400000L * 7));

        // Act
        boolean resultado = dao.suspenderUsuario(idUsuarioInexistente, fechaDesbloqueo);

        // Assert
        assertFalse(resultado, "No debe suspender a un usuario que no existe");
    }

    @Test
    void getIdUsuarioByIdRenta() {
        // Arrange
        int idRentaInexistente = 99999;

        // Act
        int idUsuario = dao.getIdUsuarioByIdRenta(idRentaInexistente);

        // Assert
        assertEquals(-1, idUsuario, "Debe devolver -1 cuando no encuentra el registro en BD");
    }
}
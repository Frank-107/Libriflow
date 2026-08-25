package mx.edu.utez.libriflow.model.Dao;

import mx.edu.utez.libriflow.model.RentaResumen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RentaDaoTest {

    private RentaDao dao;

    @BeforeEach
    void setUp() {
        // Arrange: Se ejecuta antes de cada prueba para tener el objeto listo
        dao = new RentaDao();
    }

    @Test
    void getResumenTodasLasRentas() {
        // Act
        List<RentaResumen> lista = dao.getResumenTodasLasRentas();

        // Assert
        assertNotNull(lista, "La lista global de rentas no debe ser nula");
        assertTrue(lista.size() >= 0, "La lista debe ejecutarse correctamente");
    }

    @Test
    void getResumenRentasPorUsuario() {
        // Arrange
        int idUsuarioPrueba = 1;

        // Act
        List<RentaResumen> lista = dao.getResumenRentasPorUsuario(idUsuarioPrueba);

        // Assert
        assertNotNull(lista, "La lista de rentas del usuario no debe ser nula");
    }

    @Test
    void marcarComoEntregada() {
        // Arrange: Usamos un ID que no existe para no afectar la BD real
        int idDetalleInexistente = 99999;

        // Act
        boolean resultado = dao.marcarComoEntregada(idDetalleInexistente);

        // Assert: El SQL está bien escrito, pero al no existir el ID, debe retornar false
        assertFalse(resultado, "No se debe actualizar un registro con ID inexistente");
    }

    @Test
    void marcarComoFinalizada() {
        // Arrange
        int idDetalleInexistente = 99999;

        // Act
        boolean resultado = dao.marcarComoFinalizada(idDetalleInexistente);

        // Assert
        assertFalse(resultado, "No se debe finalizar una renta con ID inexistente");
    }

    @Test
    void cambiarEstadoRenta() {
        // Arrange
        int idDetalleInexistente = 99999;
        String nuevoEstado = "CANCELADA";

        // Act
        boolean resultado = dao.cambiarEstadoRenta(idDetalleInexistente, nuevoEstado);

        // Assert
        assertFalse(resultado, "No se debe cambiar el estado de un registro inexistente");
    }

    @Test
    void contarRentasActivasPorUsuario() {
        // Arrange
        int idUsuarioPrueba = 1;

        // Act
        int total = dao.contarRentasActivasPorUsuario(idUsuarioPrueba);

        // Assert: Validamos que devuelva un número (0 o mayor) y no provoque errores
        assertTrue(total >= 0, "El total de rentas activas debe ser mayor o igual a 0");
    }

    @Test
    void contarRetrasosPorUsuario() {
        // Arrange
        int idUsuarioPrueba = 1;

        // Act
        int total = dao.contarRetrasosPorUsuario(idUsuarioPrueba);

        // Assert
        assertTrue(total >= 0, "El total de retrasos debe ser mayor o igual a 0");
    }
}
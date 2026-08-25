package mx.edu.utez.libriflow.utils;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Gestor de tareas programadas en segundo plano para la plataforma LibriFlow.
 *
 * Se encarga de administrar el ciclo de vida del ejecutor de tareas continuas
 * (como la verificación periódica de vencimiento de rentas y aplicación de penalizaciones),
 * además de garantizar la liberación limpia de recursos (pool de conexiones HikariCP y
 * desregistro de drivers JDBC) durante el apagado de la aplicación en el servidor Tomcat.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @version 1.0
 * @since 25/08/2026
 */
public class ProgramadorTareas {

    /** Pool de hilos programados para la ejecución de tareas diferidas. */
    private static ScheduledExecutorService scheduler;

    /**
     * Inicializa el servicio de ejecución en segundo plano y programa las tareas periódicas.
     *
     * Configura la tarea {@link RevisarRentas} para ejecutarse con una frecuencia diaria,
     * previniendo ejecuciones duplicadas si el servicio ya se encuentra activo.
     */
    public static void iniciar() {

        // Evita crear otro scheduler si ya existe uno activo
        if (scheduler != null && !scheduler.isShutdown()) {
            System.out.println("El programador de tareas ya está iniciado.");
            return;
        }

        // Creación del pool de un solo hilo para tareas de fondo
        scheduler = Executors.newScheduledThreadPool(1);

        // Programación de la revisión de rentas con ejecución cada 24 horas
        scheduler.scheduleAtFixedRate(
                new RevisarRentas(),
                0,
                1,
                TimeUnit.DAYS
        );

        System.out.println("Tareas programadas iniciadas.");
    }

    /**
     * Detiene el programador de tareas y realiza la limpieza de recursos de la base de datos.
     *
     * Cierra el pool de conexiones de HikariCP y desregistra manualmente los controladores
     * JDBC del {@link DriverManager} para prevenir fugas de memoria (memory leaks) en el ClassLoader
     * de Apache Tomcat al momento de reaplicar o apagar el contexto web.
     */
    public static void detener() {

        // 1. Cierre del pool de conexiones HikariCP
        try {
            SQLconnector.closeConnection();
        } catch (Exception e) {
            System.err.println("Error cerrando HikariCP: " + e.getMessage());
        }

        // 2. Desregistro preventivo de controladores JDBC (Oracle, MySQL, etc.)
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                System.out.println("Driver desregistrado con éxito: " + driver);
            } catch (SQLException e) {
                System.err.println("Error desregistrando driver: " + driver + " -> " + e.getMessage());
            }
        }

        // 3. Apagado del ScheduledExecutorService
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            System.out.println("Tareas programadas detenidas.");
        }
    }
}
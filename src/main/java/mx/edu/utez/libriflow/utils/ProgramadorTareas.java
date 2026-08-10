package mx.edu.utez.libriflow.utils;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProgramadorTareas {

    private static ScheduledExecutorService scheduler;

    public static void iniciar() {

        // Evita crear otro scheduler si ya existe uno
        if (scheduler != null && !scheduler.isShutdown()) {
            System.out.println("El programador de tareas ya está iniciado.");
            return;
        }

        scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(
                new RevisarRentas(),
                0,
                1,
                TimeUnit.DAYS
        );

        System.out.println("Tareas programadas iniciadas.");
    }

    public static void detener() {

        // 1. CERRAR EL POOL DE HIKARICP
        // Llama al método que cierra tu HikariDataSource
        try {
            SQLconnector.closeConnection();
        } catch (Exception e) {
            System.err.println("Error cerrando HikariCP: " + e.getMessage());
        }

        // 2. DESREGISTRAR DRIVERS JDBC (Oracle, MySQL, etc.)
        // Evita fugas de memoria con Tomcat y el ClassLoader
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

        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            System.out.println("Tareas programadas detenidas.");
        }
    }
}
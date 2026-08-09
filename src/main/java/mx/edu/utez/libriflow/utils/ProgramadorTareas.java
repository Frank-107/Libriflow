package mx.edu.utez.libriflow.utils;

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

        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            System.out.println("Tareas programadas detenidas.");
        }
    }
}
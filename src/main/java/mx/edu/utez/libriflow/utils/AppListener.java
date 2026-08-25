package mx.edu.utez.libriflow.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Listener del ciclo de vida de la aplicación web para LibriFlow.
 *
 * Se encarga de capturar los eventos de inicialización y destrucción del contexto
 * del servidor (Tomcat), permitiendo arrancar y detener adecuadamente servicios
 * en segundo plano como el programador de tareas automáticas (ej. revisión de rentas y penalizaciones).
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @version 1.0
 * @since 25/08/2026
 */
@WebListener
public class AppListener implements ServletContextListener {

    /**
     * Se ejecuta automáticamente cuando el contexto de la aplicación web se inicializa en el servidor.
     * Muestra el mensaje de arranque e inicia los hilos/servicios programados de fondo.
     *
     * @param sce Evento del contexto del Servlet conteniendo la información de inicio.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println("====================================");
        System.out.println("LIBRIFLOW INICIANDO");
        System.out.println("====================================");

        // Inicia el hilo de ejecución para tareas diferidas y automáticas
        ProgramadorTareas.iniciar();
    }

    /**
     * Se ejecuta automáticamente cuando la aplicación web se apaga o desaplica del servidor.
     * Garantiza el cierre seguro y ordenado de los servicios en segundo plano para evitar fugas de memoria.
     *
     * @param sce Evento del contexto del Servlet conteniendo la información del apagado.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        System.out.println("====================================");
        System.out.println("LIBRIFLOW CERRANDO");
        System.out.println("====================================");

        // Detiene el programador de tareas para liberar recursos del sistema
        ProgramadorTareas.detener();
    }
}
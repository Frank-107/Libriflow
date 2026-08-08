package mx.edu.utez.libriflow.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println("====================================");
        System.out.println("LIBRIFLOW INICIANDO");
        System.out.println("====================================");

        ProgramadorTareas.iniciar();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        System.out.println("====================================");
        System.out.println("LIBRIFLOW CERRANDO");
        System.out.println("====================================");

        ProgramadorTareas.detener();
    }
}
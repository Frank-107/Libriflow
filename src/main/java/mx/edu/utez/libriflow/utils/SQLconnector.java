package mx.edu.utez.libriflow.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestor de conexiones a la base de datos Oracle mediante el pool de alto rendimiento HikariCP.
 *
 * Esta clase se encarga de localizar la Oracle Cloud Wallet (TNS_ADMIN) de forma dinámica,
 * cargar credenciales de acceso (con prioridad en variables de entorno y fallback a propiedades),
 * e inicializar el pool de conexiones reutilizables para optimizar el acceso JDBC en la plataforma LibriFlow.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @version 1.0
 * @since 25/08/2026
 */
public class SQLconnector {

    /** Pool de conexiones de HikariCP para la gestión eficiente de sesiones JDBC. */
    private static HikariDataSource dataSource;

    static {
        try {
            // 1. Localización del directorio que contiene los certificados de Oracle Wallet
            ClassLoader classLoader = SQLconnector.class.getClassLoader();
            URL walletUrl = classLoader.getResource("wallet/");

            if (walletUrl == null) {
                throw new RuntimeException("No se encontró la Wallet de la base de datos");
            }

            String walletPath = new File(walletUrl.toURI()).getAbsolutePath();
            walletPath = walletPath.replace("\\", "/");

            // 2. Obtención de credenciales de acceso desde las variables de entorno del sistema
            String dbUser = System.getenv("DB_USER");
            String dbPass = System.getenv("DB_PASS");
            String dbName = System.getenv("DB_NAME");

            // Fallback: Si no existen en el entorno, busca en credentials.properties
            if (dbUser == null || dbPass == null || dbName == null) {
                System.out.println("Advertencia: Faltan variables de entorno de la BD. Buscando en credentials.properties...");
                Properties creds = new Properties();
                try (InputStream is = classLoader.getResourceAsStream("credentials.properties")) {
                    if (is == null) {
                        throw new RuntimeException("No se encontró el archivo credentials.properties ni las variables de entorno de la base de datos.");
                    }
                    creds.load(is);

                    if (dbUser == null) dbUser = creds.getProperty("db.user");
                    if (dbPass == null) dbPass = creds.getProperty("db.pass");
                    if (dbName == null) dbName = creds.getProperty("db.name");
                }
            }

            // Validar existencia del identificador del servicio de la base de datos
            if (dbName == null) {
                throw new RuntimeException("El nombre de la base de datos (db.name / DB_NAME) no está configurado.");
            }

            // 3. Configuración de parámetros de rendimiento y conexión del pool HikariCP
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("oracle.jdbc.OracleDriver");

            // Construcción de la cadena JDBC incluyendo la ruta TNS_ADMIN hacia la Wallet
            config.setJdbcUrl("jdbc:oracle:thin:@" + dbName + "?TNS_ADMIN=" + walletPath);

            // Asignación de credenciales
            config.setUsername(dbUser);
            config.setPassword(dbPass);

            // Ajustes del pool de conexiones
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(20000);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
            System.out.println("¡Pool de conexiones a la base de datos inicializado con éxito!");

        } catch (Exception e) {
            System.err.println("Error crítico al inicializar la base de datos");
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Proporciona una conexión activa a la base de datos administrada por el pool de HikariCP.
     *
     * @return Una instancia activa de {@link Connection}.
     * @throws SQLException Si ocurre un error de red o de autenticación con la base de datos.
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Cierra el pool de conexiones de HikariCP y libera los recursos asociados al servidor.
     * Es invocado durante el apagado del contexto web mediante la clase {@link ProgramadorTareas}.
     */
    public static void closeConnection() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
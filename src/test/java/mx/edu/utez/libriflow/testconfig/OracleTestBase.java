package mx.edu.utez.libriflow.testconfig;

import org.testcontainers.oracle.OracleContainer;
import java.time.Duration;

import mx.edu.utez.libriflow.utils.SQLconnector;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class OracleTestBase {

    protected static final OracleContainer ORACLE;

    static {

        ORACLE = new OracleContainer(
                "gvenzl/oracle-free:slim-faststart"
        )
                .withStartupTimeout(
                        Duration.ofMinutes(3)
                )
                .withInitScript(
                        "schema-test.sql"
                );

        ORACLE.start();

        System.setProperty(
                "libriflow.test.db.url",
                ORACLE.getJdbcUrl()
        );

        System.setProperty(
                "libriflow.test.db.user",
                ORACLE.getUsername()
        );

        System.setProperty(
                "libriflow.test.db.pass",
                ORACLE.getPassword()
        );

        System.out.println(
                "======================================"
        );

        System.out.println(
                "ORACLE DOCKER INICIADO"
        );

        System.out.println(
                "URL: " + ORACLE.getJdbcUrl()
        );

        System.out.println(
                "======================================"
        );
    }
    @BeforeEach
    void limpiarBaseDeDatos() {

        try (Connection con = SQLconnector.getConnection();
             Statement st = con.createStatement()) {

            st.executeUpdate("DELETE FROM RESENA");
            st.executeUpdate("DELETE FROM DETALLE_RENTA");
            st.executeUpdate("DELETE FROM DETALLE_TRANSACCION");
            st.executeUpdate("DELETE FROM TRANSACCION");
            st.executeUpdate("DELETE FROM IMAGEN");
            st.executeUpdate("DELETE FROM PUBLICACION_US");
            st.executeUpdate("DELETE FROM PUBLICACION_LF");
            st.executeUpdate("DELETE FROM ROL");
            st.executeUpdate("DELETE FROM CREDENCIAL");
            st.executeUpdate("DELETE FROM LIBRO");
            st.executeUpdate("DELETE FROM USUARIO");

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo limpiar la base de datos de pruebas",
                    e
            );
        }
    }
}
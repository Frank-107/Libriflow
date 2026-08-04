package mx.edu.utez.libriflow.model.Dao;
import mx.edu.utez.libriflow.model.DetalleTransaccion;
import mx.edu.utez.libriflow.utils.SQLconnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class DetalleTransaccionDao {

    public int create(DetalleTransaccion entidad) {
        String sql = """
        INSERT INTO Detalle_Transaccion
        (ID_TRANSACCION,
         ID_PUBLICACION_US,
         ID_PUBLICACION_LF,
         ID_VENDEDOR,
         TIPO_OPERACION,
         PRECIO,
         GANANCIA_LIBRIFLOW,
         GANANCIA_VENDEDOR)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = SQLconnector.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     new String[]{"ID_DETALLE"}
             )) {

            ps.setInt(1, entidad.getIdTransaccion());

            if (entidad.getIdPublicacionUs() != null) {
                ps.setInt(2, entidad.getIdPublicacionUs());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }

            if (entidad.getIdPublicacionLf() != null) {
                ps.setInt(3, entidad.getIdPublicacionLf());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            if(entidad.getIdVendedor() != null) {
                ps.setInt(4, entidad.getIdVendedor());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            ps.setString(5, entidad.getTipoOperacion());
            ps.setDouble(6, entidad.getPrecio());
            ps.setDouble(7, entidad.getGananciaLibriFlow());
            ps.setDouble(8, entidad.getGananciaVendedor());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new SQLException("No se pudo insertar el detalle de la transacción.");
            }

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

            throw new SQLException("No se pudo obtener el ID del detalle insertado.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
}

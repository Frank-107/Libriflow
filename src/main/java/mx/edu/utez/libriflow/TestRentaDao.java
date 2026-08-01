package mx.edu.utez.libriflow;

import mx.edu.utez.libriflow.model.Dao.RentaDao;
import mx.edu.utez.libriflow.model.RentaResumen;

import java.util.List;

public class TestRentaDao {
    public static void main(String[] args) {
        RentaDao dao = new RentaDao();
        List<RentaResumen> lista = dao.getResumenTodasLasRentas();

        System.out.println("Total rentas encontradas: " + lista.size());
        System.out.println("(Debería ser 4)");
        System.out.println("---------------------------------------");

        for (RentaResumen r : lista) {
            System.out.println(
                    "id_detalle=" + r.getIdDetalle() +
                            " | titulo=" + r.getTitulo() +
                            " | autor=" + r.getAutor() +
                            " | estado=" + r.getEstado() +
                            " | comprador=" + r.getNombreComprador() +
                            " | vendedor=" + r.getNombreVendedor() +
                            " | precio=" + r.getPrecio()
            );
        }
    }
}
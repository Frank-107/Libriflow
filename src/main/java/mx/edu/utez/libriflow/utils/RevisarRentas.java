package mx.edu.utez.libriflow.utils;

import mx.edu.utez.libriflow.model.Dao.DetalleRentaDao;
import mx.edu.utez.libriflow.model.DetalleRenta;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RevisarRentas implements Runnable {

    private final DetalleRentaDao detalleRentaDao = new DetalleRentaDao();

    @Override
    public void run() {

        try {

            System.out.println("REVISANDO RENTAS");

            Timestamp ahora = Timestamp.valueOf(LocalDateTime.now());

            // Obtener todas las rentas activas
            List<DetalleRenta> rentas =
                    detalleRentaDao.getRentasActivas();

            for (DetalleRenta renta : rentas) {

                // Si ya pasó la fecha límite
                if (renta.getFechaLimite().before(ahora) && renta.getPenalizacion()<1) {

                    // Cambiar estado a "Retrasada"
                    detalleRentaDao.cambiarPenalizacion(
                            renta.getIdDetalle(),
                            1
                    );

                    System.out.println(
                            "Renta " + renta.getIdDetalle()
                                    + " cambió a estado 'Retrasada'."
                    );
                }
            }

            // Revisar rentas que ya tienen retraso
            List<DetalleRenta> rentasRetrasadas =
                    detalleRentaDao.getRentasRetrasadasActivas();

            for (DetalleRenta renta : rentasRetrasadas) {
                System.out.println(
                        "Renta " + renta.getIdDetalle()
                                + " está retrasada desde "
                                + renta.getFechaLimite()
                );

                long diasRetraso = ChronoUnit.DAYS.between(
                        renta.getFechaLimite().toLocalDateTime().toLocalDate(),
                        ahora.toLocalDateTime().toLocalDate()
                );
                System.out.println("y tiente estos dias de retraso:"+diasRetraso);
                if (diasRetraso > 3 &&
                        renta.getPenalizacion() < 2) {
                    System.out.println("hay una renta con mas de 3 dias de retraso activa ");
                    detalleRentaDao.cambiarPenalizacion(
                            renta.getIdDetalle(),
                            2
                    );
                    int idUsuario = detalleRentaDao.getIdUsuarioByIdRenta(
                            renta.getIdDetalle()
                    );
                    if(idUsuario != -1) {
                        detalleRentaDao.suspenderUsuario(idUsuario, Timestamp.valueOf(LocalDateTime.now().plusDays(3)));
                        System.out.println(
                                "Renta " + renta.getIdDetalle()
                                        + " recibió penalización 2."
                        );
                    }else {
                        System.out.println("No se pudo obtener el ID del usuario para la renta " + renta.getIdDetalle());
                    }

                    System.out.println(
                            "Renta " + renta.getIdDetalle()
                                    + " recibió penalización 2."
                    );
                }
            }

            System.out.println("Revisión de rentas terminada.");

        } catch (Exception e) {

            System.out.println("====================================");
            System.out.println("ERROR AL REVISAR RENTAS");
            System.out.println("====================================");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
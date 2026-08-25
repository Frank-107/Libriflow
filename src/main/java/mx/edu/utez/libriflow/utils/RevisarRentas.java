package mx.edu.utez.libriflow.utils;

import mx.edu.utez.libriflow.model.Dao.DetalleRentaDao;
import mx.edu.utez.libriflow.model.DetalleRenta;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Tarea ejecutable en segundo plano ({@link Runnable}) encargada del monitoreo
 * y gestión automática del estado de las rentas en LibriFlow.
 *
 * Se encarga de evaluar periódicamente las rentas activas, identificar entregas
 * fuera de plazo, escalar niveles de penalización (Nivel 1: Retraso leve, Nivel 2: Retraso grave)
 * y aplicar suspensiones temporales a las cuentas de usuarios que excedan el límite tolerado.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @version 1.0
 * @since 25/08/2026
 */
public class RevisarRentas implements Runnable {

    /** Objeto de acceso a datos para la gestión y actualización de rentas. */
    private final DetalleRentaDao detalleRentaDao = new DetalleRentaDao();

    /**
     * Ejecuta el proceso automatizado de auditoría sobre las rentas activas.
     *
     * El flujo de trabajo realiza dos verificaciones principales:
     * <ul>
     *   <li><b>Fase 1:</b> Detecta rentas con fecha límite vencida sin penalización previa y les asigna el Nivel 1 (Retrasada).</li>
     *   <li><b>Fase 2:</b> Evalúa rentas retrasadas con más de 3 días acumulados, asigna el Nivel 2 de penalización
     *       y suspende al usuario de la plataforma durante 3 días.</li>
     * </ul>
     */
    @Override
    public void run() {

        try {

            System.out.println("REVISANDO RENTAS");

            Timestamp ahora = Timestamp.valueOf(LocalDateTime.now());

            // 1. Obtención de todas las rentas vigentes para validación de fecha límite
            List<DetalleRenta> rentas =
                    detalleRentaDao.getRentasActivas();

            for (DetalleRenta renta : rentas) {

                // Si la fecha actual superó la fecha límite y aún no registra penalización
                if (renta.getFechaLimite().before(ahora) && renta.getPenalizacion() < 1) {

                    // Actualiza el estado a penalización Nivel 1 ("Retrasada")
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

            // 2. Auditoría sobre rentas que ya se encuentran marcadas como retrasadas
            List<DetalleRenta> rentasRetrasadas =
                    detalleRentaDao.getRentasRetrasadasActivas();

            for (DetalleRenta renta : rentasRetrasadas) {
                System.out.println(
                        "Renta " + renta.getIdDetalle()
                                + " está retrasada desde "
                                + renta.getFechaLimite()
                );

                // Cálculo en días transcurridos desde la fecha límite hasta el momento actual
                long diasRetraso = ChronoUnit.DAYS.between(
                        renta.getFechaLimite().toLocalDateTime().toLocalDate(),
                        ahora.toLocalDateTime().toLocalDate()
                );
                System.out.println("y tiene estos días de retraso: " + diasRetraso);

                // Si el retraso supera los 3 días y no se ha aplicado el Nivel 2 de penalización
                if (diasRetraso > 3 && renta.getPenalizacion() < 2) {
                    System.out.println("hay una renta con más de 3 días de retraso activa ");

                    // Escalar la renta a penalización Nivel 2
                    detalleRentaDao.cambiarPenalizacion(
                            renta.getIdDetalle(),
                            2
                    );

                    int idUsuario = detalleRentaDao.getIdUsuarioByIdRenta(
                            renta.getIdDetalle()
                    );

                    // Si se encuentra al titular de la renta, se efectúa la suspensión por 3 días
                    if (idUsuario != -1) {
                        detalleRentaDao.suspenderUsuario(idUsuario, Timestamp.valueOf(LocalDateTime.now().plusDays(3)));
                        System.out.println(
                                "Renta " + renta.getIdDetalle()
                                        + " recibió penalización 2."
                        );
                    } else {
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
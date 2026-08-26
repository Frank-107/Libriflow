/**
 * @fileoverview Control para la confirmación de baja de publicaciones en LibriFlow.
 *
 * Escucha la interacción del usuario en la ventana modal de confirmación y desencadena
 * el envío del formulario correspondiente para retirar una publicación del sistema.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @version 1.0
 * @since 25/08/2026
 */

document.addEventListener("DOMContentLoaded", function() {
    // 1. Obtención de referencias del DOM para el botón de confirmación y el formulario
    const btnConfirmarBaja = document.getElementById('btnConfirmarBaja');
    const formBajaPublicacion = document.getElementById('formBajaPublicacion');

    // Cláusula de guarda: verifica la existencia de ambos elementos antes de asignar los eventos
    if (btnConfirmarBaja && formBajaPublicacion) {

        /**
         * Asigna el evento de clic al botón dentro de la ventana modal
         * para realizar el envío definitivo del formulario de baja al servidor.
         */
        btnConfirmarBaja.addEventListener('click', function() {
            formBajaPublicacion.submit();
        });
    }
});
/**
 * @fileoverview Calculadora de comisión y ganancia estimada en tiempo real para publicaciones de usuarios en LibriFlow.
 *
 * Monitorea el campo de entrada del precio de venta, limpia validaciones nativas del navegador
 * y calcula de manera dinámica la comisión del 15% para la plataforma junto con la ganancia neta (85%) que recibirá el vendedor.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @version 1.0
 * @since 25/08/2026
 */

window.onload = function() {
    // 1. Obtención de la referencia al campo de texto del precio por ID o selector de nombre
    var inputPrecio = document.getElementById('precio') || document.querySelector('input[name="precio"]');

    if (inputPrecio) {
        /**
         * Manejador de eventos 'input' para calcular la desglose financiero en tiempo real.
         */
        inputPrecio.oninput = function() {
            // Restablece la restricción nativa del navegador para permitir la entrada de nuevos valores sin bloqueos
            inputPrecio.setCustomValidity("");

            var precio = parseFloat(inputPrecio.value);
            var mensaje15 = document.getElementById("mensaje15");

            if (mensaje15) {
                // Si el precio ingresado es un número válido y mayor a cero, realiza el desglose de montos
                if (precio > 0) {
                    var comision = (precio * 0.15).toFixed(2);
                    var ganancia = (precio * 0.85).toFixed(2);
                    mensaje15.innerHTML = "Se aplicará el 15% de comisión ($" + comision + "). Recibirás: $" + ganancia + " MXN";
                } else {
                    // Oculta/Limpia el mensaje descriptivo si el valor es cero, negativo o está vacío
                    mensaje15.innerHTML = "";
                }
            }
        };
    }
};
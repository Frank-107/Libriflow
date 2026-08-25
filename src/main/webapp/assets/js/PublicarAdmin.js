/**
 * @fileoverview Validaciones del formulario de publicación de libros para LibriFlow.
 *
 * Garantiza que el usuario seleccione al menos una modalidad de disponibilidad (Venta, Renta o ambas)
 * mediante la API de Custom Validity de HTML5 antes de procesar el registro del libro,
 * administrando el restablecimiento visual del botón de envío ante errores de validación.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @version 1.0
 * @since 25/08/2026
 */

document.addEventListener("DOMContentLoaded", function () {
    // 1. Obtención de referencias al formulario principal y casillas de selección de modalidad
    const formulario = document.getElementById("formPublicar");
    const checkVenta = document.getElementById("checkVenta");
    const checkRenta = document.getElementById("checkRenta");

    if (formulario) {
        /**
         * Evalúa las restricciones requeridas al presionar la acción de publicar.
         */
        formulario.addEventListener("submit", function (e) {
            const btnSubmit = formulario.querySelector('.btn-submit');

            // Verifica que al menos un checkbox (Venta o Renta) esté marcado
            if (checkVenta && checkRenta && !checkVenta.checked && !checkRenta.checked) {
                checkVenta.setCustomValidity("Debes seleccionar el tipo de publicación (Venta, Renta o ambas).");
            } else if (checkVenta) {
                // Remueve la restricción personalizada para permitir el envío regular
                checkVenta.setCustomValidity("");
            }

            // Si la comprobación de validez nativa del navegador falla, habilita nuevamente el botón de envío
            if (!formulario.checkValidity()) {
                if (btnSubmit) {
                    btnSubmit.disabled = false;
                    btnSubmit.innerHTML = 'Publicar';
                }
            }
        });
    }
});
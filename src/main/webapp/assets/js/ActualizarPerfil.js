/**
 * @fileoverview Control de actualización de perfil de usuario para LibriFlow.
 *
 * Gestiona la detección de cambios en tiempo real dentro del formulario de edición de perfil,
 * habilita/deshabilita dinámicamente el botón de guardado y coordina la visualización
 * del modal de confirmación de Bootstrap antes de realizar el envío definitivo.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @version 1.0
 * @since 25/08/2026
 */

document.addEventListener("DOMContentLoaded", function() {
    // 1. Obtención de referencias del DOM para el formulario y el modal
    const formulario = document.getElementById("formActualizarPerfil");
    const btnGuardar = document.getElementById("btnActualizarForm");
    const btnConfirmar = document.getElementById("btnConfirmarSubmit");
    const ventanaModal = document.getElementById("modalConfirmarActualizacion");

    // Clausula de guarda: previene errores de ejecución si los elementos no existen en la vista actual
    if (!formulario || !btnGuardar || !btnConfirmar || !ventanaModal) return;

    // Inicialización del componente Modal de Bootstrap 5
    const modal = new bootstrap.Modal(ventanaModal);
    const inputs = formulario.querySelectorAll("input");
    const valoresIniciales = {};

    // 2. Captura del estado inicial del formulario para comparar cambios posteriores
    inputs.forEach(input => {
        valoresIniciales[input.name] = input.value;
    });

    /**
     * Escuchador de eventos de entrada ('input') en el formulario.
     * Compara los valores actuales contra el estado inicial para activar el botón de guardado únicamente si hubo cambios.
     */
    formulario.addEventListener("input", function() {
        let hayCambios = false;

        inputs.forEach(input => {
            if (input.value !== valoresIniciales[input.name]) {
                hayCambios = true;
            }
        });

        btnGuardar.disabled = !hayCambios;
    });

    /**
     * Intercepta el evento de envío nativo del formulario para evitar el submit inmediato
     * y desplegar la ventana modal de confirmación.
     */
    formulario.addEventListener("submit", function(e) {
        e.preventDefault();
        modal.show();
    });

    /**
     * Procesa la confirmación del usuario dentro del modal.
     * Deshabilita el botón de acción para prevenir doble envío y envía el formulario al servidor.
     */
    btnConfirmar.addEventListener("click", function() {
        btnConfirmar.disabled = true;
        btnConfirmar.innerHTML = 'Actualizando...';
        formulario.submit();
    });
});
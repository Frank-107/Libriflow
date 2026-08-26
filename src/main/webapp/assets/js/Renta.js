/**
 * @fileoverview Calculadora dinámica de tarifas y control de períodos de renta para LibriFlow.
 *
 * Determina el costo total de alquiler de libros en función del rango de fechas seleccionado,
 * restringiendo la fecha inicial a partir del día actual y fijando un límite máximo de renta de 14 días.
 * Aplica una tarifa escalonada ($5 MXN/día durante los primeros 7 días y $3 MXN/día por cada día adicional).
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @version 1.0
 * @since 25/08/2026
 */

document.addEventListener("DOMContentLoaded", function () {
    // 1. Obtención de referencias del DOM para las fechas, montos y botón de confirmación
    const hoy = new Date();
    const today = hoy.toISOString().split('T')[0];
    const fechaInicio = document.getElementById('fechaInicio');
    const fechaFin = document.getElementById('fechaFin');
    const montoMostrado = document.getElementById('montoMostrado');
    const precioRentaInput = document.getElementById('precioRentaInput');
    const btnConfirmarRenta = document.getElementById('btnConfirmarRenta');

    // Cláusula de guarda: si no se encuentran los controles de fecha en la vista, finaliza la ejecución
    if (!fechaInicio || !fechaFin) return;

    // Restricción inicial: imposibilita seleccionar fechas anteriores a la fecha actual
    fechaInicio.setAttribute('min', today);
    fechaFin.setAttribute('min', today);

    /**
     * Calcula el monto total a pagar según el número de días del período seleccionado
     * y actualiza el valor visual, el campo oculto del formulario y el estado del botón.
     */
    function calcularRenta() {
        if (!fechaInicio.value || !fechaFin.value) {
            btnConfirmarRenta.disabled = true;
            montoMostrado.textContent = "0.0";
            return;
        }

        const inicio = new Date(fechaInicio.value + "T00:00:00");
        const fin = new Date(fechaFin.value + "T23:59:59");

        // Cálculo de diferencia en milisegundos convertida a días enteros (incluyendo el día de inicio)
        const diferenciaTiempo = fin.getTime() - inicio.getTime();
        const dias = Math.ceil(diferenciaTiempo / (1000 * 3600 * 24)) + 1;

        if (dias <= 0) {
            montoMostrado.textContent = "0.0";
            btnConfirmarRenta.disabled = true;
            return;
        }

        // Esquema tarifario escalonado:
        // - Primeros 7 días: $5.00 MXN por día
        // - Día 8 en adelante: $3.00 MXN por día adicional
        let total = 0;
        if (dias <= 7) {
            total = dias * 5;
        } else {
            total = (7 * 5) + ((dias - 7) * 3);
        }

        const totalFormateado = total.toFixed(2);
        montoMostrado.textContent = totalFormateado;
        precioRentaInput.value = totalFormateado;
        btnConfirmarRenta.disabled = false;
    }

    // 2. Asignación de escuchadores de eventos sobre los controles de fecha
    ['change', 'input'].forEach(tipoEvento => {
        /**
         * Reajusta dinámicamente el límite mínimo y máximo (máximo 14 días en total) del campo de fecha final
         * cada vez que se modifica la fecha de inicio.
         */
        fechaInicio.addEventListener(tipoEvento, function () {
            fechaFin.setAttribute('min', fechaInicio.value);

            const fechaMaxima = new Date(fechaInicio.value + "T00:00:00");
            fechaMaxima.setDate(fechaMaxima.getDate() + 13); // Define la ventana de renta en máximo 14 días

            const max = fechaMaxima.toISOString().split('T')[0];
            fechaFin.setAttribute('max', max);

            calcularRenta();
        });

        fechaFin.addEventListener(tipoEvento, calcularRenta);
    });
});
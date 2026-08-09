document.addEventListener("DOMContentLoaded", function () {
    const hoy = new Date();
    const today = hoy.toISOString().split('T')[0];
    const fechaInicio = document.getElementById('fechaInicio');
    const fechaFin = document.getElementById('fechaFin');
    const montoMostrado = document.getElementById('montoMostrado');
    const precioRentaInput = document.getElementById('precioRentaInput');
    const btnConfirmarRenta = document.getElementById('btnConfirmarRenta');

    if (!fechaInicio || !fechaFin) return;

    fechaInicio.setAttribute('min', today);
    fechaFin.setAttribute('min', today);

    function calcularRenta() {
        if (!fechaInicio.value || !fechaFin.value) {
            btnConfirmarRenta.disabled = true;
            montoMostrado.textContent = "0.0";
            return;
        }

        const inicio = new Date(fechaInicio.value + "T00:00:00");
        const fin = new Date(fechaFin.value + "T23:59:59");

        const diferenciaTiempo = fin.getTime() - inicio.getTime();
        const dias = Math.ceil(diferenciaTiempo / (1000 * 3600 * 24)) + 1;

        if (dias <= 0) {
            montoMostrado.textContent = "0.0";
            btnConfirmarRenta.disabled = true;
            return;
        }

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

    ['change', 'input'].forEach(tipoEvento => {
        fechaInicio.addEventListener(tipoEvento, function () {
            fechaFin.setAttribute('min', fechaInicio.value);

            const fechaMaxima = new Date(fechaInicio.value + "T00:00:00");
            fechaMaxima.setDate(fechaMaxima.getDate() + 13);

            const max = fechaMaxima.toISOString().split('T')[0];
            fechaFin.setAttribute('max', max);

            calcularRenta();
        });

        fechaFin.addEventListener(tipoEvento, calcularRenta);
    });
});
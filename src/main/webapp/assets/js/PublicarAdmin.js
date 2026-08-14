document.addEventListener("DOMContentLoaded", function () {
    const formulario = document.getElementById("formPublicar");
    const checkVenta = document.getElementById("checkVenta");
    const checkRenta = document.getElementById("checkRenta");

    if (formulario) {
        formulario.addEventListener("submit", function (e) {
            const btnSubmit = formulario.querySelector('.btn-submit');

            if (checkVenta && checkRenta && !checkVenta.checked && !checkRenta.checked) {
                checkVenta.setCustomValidity("Debes seleccionar el tipo de publicación (Venta, Renta o ambas).");
            } else if (checkVenta) {
                checkVenta.setCustomValidity("");
            }

            if (!formulario.checkValidity()) {
                if (btnSubmit) {
                    btnSubmit.disabled = false;
                    btnSubmit.innerHTML = 'Publicar';
                }
            }
        });
    }
});
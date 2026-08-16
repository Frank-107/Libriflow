document.addEventListener("DOMContentLoaded", function() {
    const btnConfirmarBaja = document.getElementById('btnConfirmarBaja');
    const formBajaPublicacion = document.getElementById('formBajaPublicacion');

    if (btnConfirmarBaja && formBajaPublicacion) {
        btnConfirmarBaja.addEventListener('click', function() {
            formBajaPublicacion.submit();
        });
    }
});
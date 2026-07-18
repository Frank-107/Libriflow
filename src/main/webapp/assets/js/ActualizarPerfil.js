document.addEventListener("DOMContentLoaded", function() {
    const formulario = document.getElementById("formActualizarPerfil");
    const btnGuardar = document.getElementById("btnActualizarForm");
    const btnConfirmar = document.getElementById("btnConfirmarSubmit");
    const ventanaModal = document.getElementById("modalConfirmarActualizacion");

    if (!formulario || !btnGuardar || !btnConfirmar || !ventanaModal) return;

    const modal = new bootstrap.Modal(ventanaModal);
    const inputs = formulario.querySelectorAll("input");
    const valoresIniciales = {};

    inputs.forEach(input => {
        valoresIniciales[input.name] = input.value;
    });

    formulario.addEventListener("input", function() {
        let hayCambios = false;

        inputs.forEach(input => {
            if (input.value !== valoresIniciales[input.name]) {
                hayCambios = true;
            }
        });

        btnGuardar.disabled = !hayCambios;
    });

    formulario.addEventListener("submit", function(e) {
        e.preventDefault();
        modal.show();
    });

    btnConfirmar.addEventListener("click", function() {
        btnConfirmar.disabled = true;
        btnConfirmar.innerHTML = 'Actualizando...';
        formulario.submit();
    });
});
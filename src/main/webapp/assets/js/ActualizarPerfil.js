document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("formActualizarPerfil");
    const btnSubmit = document.getElementById("btnActualizarForm");
    const btnConfirmar = document.getElementById("btnConfirmarSubmit");
    const modalElement = document.getElementById('modalConfirmarActualizacion');

    if(form && btnSubmit && modalElement) {
        const modalConfirmacion = new bootstrap.Modal(modalElement);
        const inputs = form.querySelectorAll("input");
        const initialValues = {};

        // 1. Guardar estado inicial de los campos
        inputs.forEach(input => {
            initialValues[input.name] = input.value;
        });

        // 2. Escuchar cambios en el formulario
        form.addEventListener("input", function() {
            let hasChanged = false;
            inputs.forEach(input => {
                if (input.value !== initialValues[input.name]) {
                    hasChanged = true;
                }
            });

            // Habilitar o deshabilitar botón dependiendo si hubo cambios
            btnSubmit.disabled = !hasChanged;
        });

        // 3. Interceptar el submit del formulario para mostrar el Modal
        form.addEventListener("submit", function(e) {
            e.preventDefault(); // Detenemos el envío
            modalConfirmacion.show(); // Mostramos nuestro modal
        });

        // 4. Si el usuario confirma en el modal, enviamos el formulario
        btnConfirmar.addEventListener("click", function() {
            btnConfirmar.disabled = true;
            btnConfirmar.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Actualizando...';
            form.submit(); // Enviamos el formulario de verdad
        });
    }
});
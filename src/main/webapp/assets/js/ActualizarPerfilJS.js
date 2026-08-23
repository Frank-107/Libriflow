/**
 * @file Gestiona la lógica de la vista "Actualizar Perfil".
 * @description Controla la detección de cambios en el formulario de perfil para habilitar
 * el botón de guardado. Además, maneja la apertura del modal de confirmación y procesa
 * la actualización de datos mediante una petición asíncrona (Fetch API), mostrando
 * alertas dinámicas de éxito o error sin recargar la página.
 *
 * @author Alejandro Mena Pereyda
 * @since 23/08/2026
 */
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('formActualizarPerfil');
    const btnAbrirModal = document.getElementById('btnAbrirModal');
    const btnConfirmarSubmit = document.getElementById('btnConfirmarSubmit');
    const modalElement = document.getElementById('modalConfirmarActualizacion');

    if (!form || !btnAbrirModal || !btnConfirmarSubmit || !modalElement) return;

    form.addEventListener('submit', (e) => {
        e.preventDefault();
    });

    const inputs = form.querySelectorAll('input:not([readonly])');
    const valoresIniciales = {};

    inputs.forEach(input => {
        valoresIniciales[input.name] = input.value;
    });

    /**
     * Compara los valores actuales de los inputs con los valores iniciales.
     * Habilita el botón de abrir modal solo si el usuario ha realizado algún cambio.
     */
    function verificarCambios() {
        let hayCambios = false;
        inputs.forEach(input => {
            if (input.value !== valoresIniciales[input.name]) {
                hayCambios = true;
            }
        });
        btnAbrirModal.disabled = !hayCambios;
    }

    inputs.forEach(input => {
        input.addEventListener('input', verificarCambios);
    });

    btnAbrirModal.addEventListener('click', (e) => {
        e.preventDefault();
        const modal = new bootstrap.Modal(modalElement);
        modal.show();
    });

    btnConfirmarSubmit.addEventListener('click', (e) => {
        e.preventDefault();

        const modalInstance = bootstrap.Modal.getInstance(modalElement);
        if (modalInstance) {
            modalInstance.hide();
        }

        const formData = new FormData(form);
        const params = new URLSearchParams(formData);

        fetch('actualizar-perfil-js', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
            },
            body: params
        })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    mostrarAlerta('exito', data.message);

                    if (data.nombre) {
                        const headerNombre = document.getElementById('headerNombreUsuario');
                        if (headerNombre) {
                            headerNombre.textContent = data.nombre;
                        }
                    }

                    const inputPass1 = document.getElementById('inputNuevaContrasena');
                    const inputPass2 = document.getElementById('inputConfirmarContrasena');
                    if (inputPass1) inputPass1.value = '';
                    if (inputPass2) inputPass2.value = '';

                    inputs.forEach(input => {
                        valoresIniciales[input.name] = input.value;
                    });
                    btnAbrirModal.disabled = true;
                } else {
                    mostrarAlerta('error', data.message);
                }
            })
            .catch(() => {
                mostrarAlerta('error', 'Ocurrió un error inesperado de conexión.');
            });
    });

    /**
     * Construye y muestra una alerta visual en la interfaz del usuario,
     * desplazando la pantalla hacia arriba para asegurar que sea visible.
     *
     * @param {string} tipo - El tipo de alerta (debe ser 'error' o 'exito').
     * @param {string} mensaje - El texto que se mostrará dentro de la alerta.
     */
    function mostrarAlerta(tipo, mensaje) {
        const contenedor = document.getElementById('contenedorAlertas');
        if (!contenedor) return;

        const esError = tipo === 'error';
        const claseToast = esError ? 'libri-toast-error' : 'libri-toast-success';
        const icono = esError ? 'bi-exclamation-circle-fill' : 'bi-check-circle-fill';

        contenedor.innerHTML = `
            <div class="libri-toast ${claseToast} mb-3">
                <i class="bi ${icono} fs-5"></i>
                <span>${mensaje}</span>
            </div>
        `;

        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
});
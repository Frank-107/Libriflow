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
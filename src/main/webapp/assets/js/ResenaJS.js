document.addEventListener('DOMContentLoaded', () => {
    const btnEnviarResena = document.getElementById('btnEnviarResena');
    const formCrearResena = document.getElementById('formCrearResena');
    const contenedorResenas = document.getElementById('contenedorResenas');
    const alertaSinResenas = document.getElementById('alertaSinResenas');
    const templateResena = document.getElementById('templateResena');
    const templateAlerta = document.getElementById('templateAlerta');
    const contenedorAlertasJS = document.getElementById('contenedorAlertasJS');

    if (!btnEnviarResena || !formCrearResena) return;

    formCrearResena.addEventListener('submit', (e) => {
        e.preventDefault();

        const formData = new FormData(formCrearResena);
        const params = new URLSearchParams(formData);

        btnEnviarResena.disabled = true;

        fetch('resena-js', {
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
                    formCrearResena.reset();

                    if (alertaSinResenas) {
                        alertaSinResenas.style.display = 'none';
                    }

                    if (templateResena && contenedorResenas) {
                        const clon = templateResena.content.cloneNode(true);

                        clon.querySelector('.nombre-usuario').textContent = data.nombreUsuario || 'Usuario';
                        clon.querySelector('.texto-comentario').textContent = data.comentario;

                        const estrellasContenedor = clon.querySelector('.estrellas-contenedor');
                        for (let i = 1; i <= 5; i++) {
                            const iTag = document.createElement('i');
                            if (i <= data.calificacion) {
                                iTag.className = 'bi bi-star-fill text-warning';
                            } else {
                                iTag.className = 'bi bi-star text-muted';
                            }
                            estrellasContenedor.appendChild(iTag);
                        }

                        contenedorResenas.insertBefore(clon, contenedorResenas.firstChild);
                    }
                } else {
                    mostrarAlerta('error', data.message);
                }
            })
            .catch(() => {
                mostrarAlerta('error', 'Ocurrió un error inesperado al conectar con el servidor.');
            })
            .finally(() => {
                btnEnviarResena.disabled = false;
            });
    });

    function mostrarAlerta(tipo, mensaje) {
        if (!contenedorAlertasJS || !templateAlerta) return;

        const clon = templateAlerta.content.cloneNode(true);
        const toastDiv = clon.querySelector('.toast-alerta');
        const icon = clon.querySelector('.icono-alerta');
        const span = clon.querySelector('.texto-alerta');

        span.textContent = mensaje;

        if (tipo === 'error') {
            toastDiv.classList.add('libri-toast-error');
            icon.classList.add('bi-exclamation-circle-fill');
        } else {
            toastDiv.classList.add('libri-toast-success');
            icon.classList.add('bi-check-circle-fill');
        }

        contenedorAlertasJS.innerHTML = '';
        contenedorAlertasJS.appendChild(clon);
    }
});
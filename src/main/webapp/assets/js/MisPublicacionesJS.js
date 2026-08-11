function cancelarPublicacion(idPublicacion, botonElemento) {
    botonElemento.disabled = true;
    botonElemento.innerHTML = '<i class="bi bi-hourglass-split"></i> Cancelando...';

    fetch(`mis-publicaciones-js?idPublicacion=${idPublicacion}`, {
        method: 'DELETE'
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                const tarjeta = botonElemento.closest('.publicacion-card');
                tarjeta.remove();

                const spanTotal = document.getElementById('total-publicaciones');
                let total = parseInt(spanTotal.textContent) - 1;
                spanTotal.textContent = total;

                if (total === 0) {
                    const listaPublicaciones = document.getElementById('lista-publicaciones');
                    listaPublicaciones.innerHTML = `
                    <div class="sin-publicaciones">
                        <i class="bi bi-journal-x"></i>
                        <h4>No tienes publicaciones.</h4>
                        <p>Cuando publiques un libro aparecerá aquí.</p>
                    </div>
                `;
                    listaPublicaciones.className = '';
                }

                mostrarNotificacionDinamica('success', data.message);
            } else {
                botonElemento.disabled = false;
                botonElemento.innerHTML = '<i class="bi bi-trash"></i> Cancelar publicación';
                mostrarNotificacionDinamica('error', data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            botonElemento.disabled = false;
            botonElemento.innerHTML = '<i class="bi bi-trash"></i> Cancelar publicación';
            mostrarNotificacionDinamica('error', 'Ocurrió un error de conexión al servidor.');
        });
}

function mostrarNotificacionDinamica(tipo, mensaje) {
    const contenedor = document.getElementById('contenedor-notificaciones');
    const toast = document.createElement('div');
    toast.className = tipo === 'success'
        ? 'libri-toast libri-toast-success'
        : 'libri-toast libri-toast-error';

    const icono = tipo === 'success'
        ? '<i class="bi bi-check-circle-fill fs-5"></i>'
        : '<i class="bi bi-exclamation-circle-fill fs-5"></i>';

    toast.innerHTML = `${icono}<span>${mensaje}</span>`;
    contenedor.appendChild(toast);

    setTimeout(() => {
        toast.classList.add('show');
    }, 100);

    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => {
            toast.remove();
        }, 400);
    }, 3500);
}
/**
 * @file Gestiona la lógica de interacción de la vista "Mis Publicaciones".
 * @description Contiene las funciones para cancelar una publicación pendiente o rechazada
 * mediante peticiones asíncronas (AJAX), actualizando el DOM en tiempo real (eliminando la tarjeta
 * y actualizando contadores) y mostrando notificaciones dinámicas al usuario.
 *
 * @author Alejandro Mena Pereyda
 * @since 23/08/2026
 */

/**
 * Envía una petición DELETE al servidor para cancelar una publicación específica.
 * Si la operación es exitosa, elimina la tarjeta correspondiente del DOM,
 * actualiza el contador total de publicaciones y muestra un mensaje de éxito.
 * Si falla, restaura el estado del botón y muestra un mensaje de error.
 *
 * @param {number|string} idPublicacion - El identificador único de la publicación a cancelar.
 * @param {HTMLElement} botonElemento - El elemento HTML (botón) que disparó la acción, utilizado para bloquearlo y mostrar el estado de carga.
 */
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

/**
 * Crea, renderiza y destruye dinámicamente una notificación visual (toast) en la interfaz del usuario.
 * La notificación se añade al contenedor designado y desaparece automáticamente mediante animaciones CSS.
 *
 * @param {string} tipo - Determina el estilo visual de la alerta (ej. 'success' para éxito, 'error' para fallos).
 * @param {string} mensaje - El texto informativo que leerá el usuario.
 */
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
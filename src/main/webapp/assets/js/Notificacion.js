/**
 * @fileoverview Gestor de notificaciones emergentes (Toast) para LibriFlow.
 *
 * Controla el ciclo de vida visual de los componentes de notificación (.libri-toast):
 * automatiza la animación de entrada al cargar la página, mantiene la visibilidad
 * por un tiempo determinado y efectúa una salida suave antes de remover el elemento del DOM.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @version 1.0
 * @since 25/08/2026
 */

document.addEventListener("DOMContentLoaded", function () {
    // 1. Selecciona todos los elementos toast presentes en el DOM
    const toasts = document.querySelectorAll('.libri-toast');

    toasts.forEach((toast) => {
        // 2. Animación de entrada: agrega la clase 'show' con un ligero retardo (100 ms)
        setTimeout(() => {
            toast.classList.add('show');
        }, 100);

        // 3. Temporizador de visibilidad (3.5 segundos) antes de iniciar la ocultación
        setTimeout(() => {
            toast.classList.remove('show');

            // 4. Remueve completamente el nodo del DOM tras concluir la transición CSS (400 ms)
            setTimeout(() => {
                toast.remove();
            }, 400);
        }, 3500);
    });
});
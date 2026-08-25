/**
 * @fileoverview Validador de imágenes y gestor de vistas previas múltiples para LibriFlow.
 *
 * Itera sobre los campos de carga de imágenes (1 a 3), aplicando validaciones estrictas
 * de extensión (PNG, JPG, JPEG), tipo MIME y peso máximo (2 MB). Si las imágenes son válidas,
 * genera la vista previa dinámica mediante FileReader y despliega notificaciones de error en caso contrario.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @version 1.0
 * @since 25/08/2026
 */

/** Tipos MIME autorizados para la subida de imágenes */
const TIPOS_PERMITIDOS = ['image/png', 'image/jpeg'];

/** Extensiones de archivo válidas */
const EXTENSIONES_PERMITIDAS = ['png', 'jpg', 'jpeg'];

/** Límite de tamaño máximo permitido en Megabytes */
const TAMANO_MAXIMO_MB = 2;

/** Límite de tamaño máximo en Bytes (2 MB) */
const TAMANO_MAXIMO_BYTES = TAMANO_MAXIMO_MB * 1024 * 1024;

// Iteración sobre los 3 campos de carga de imágenes obligatorios en el formulario
for (let i = 1; i <= 3; i++) {
    const inputArchivo = document.getElementById('imagen' + i);
    const etiquetaImagen = document.getElementById('vistapreviaImg' + i);

    // Cláusula de guarda: si no existe la entrada o el visor de imagen en la plantilla, salta a la siguiente iteración
    if (!inputArchivo || !etiquetaImagen) continue;

    /**
     * Escucha el cambio de estado en la selección de archivos e inicia la serie de validaciones.
     */
    inputArchivo.addEventListener('change', function(evento) {
        const archivo = evento.target.files[0];

        if (!archivo) {
            mostrarNotificacionDinamica('error', 'Por favor, selecciona un archivo de imagen válido.');
            return;
        }

        // 1. Validar extensión de archivo
        const extension = archivo.name.split('.').pop().toLowerCase();
        if (!EXTENSIONES_PERMITIDAS.includes(extension)) {
            mostrarNotificacionDinamica('error', 'Formato no permitido. Solo se aceptan imágenes PNG, JPG o JPEG.');
            inputArchivo.value = '';
            return;
        }

        // 2. Validar tipo MIME oficial del archivo
        if (!TIPOS_PERMITIDOS.includes(archivo.type)) {
            mostrarNotificacionDinamica('error', 'El archivo no es una imagen válida (PNG, JPG o JPEG).');
            inputArchivo.value = '';
            return;
        }

        // 3. Validar tamaño máximo permitido (Límite de 2 MB)
        if (archivo.size > TAMANO_MAXIMO_BYTES) {
            mostrarNotificacionDinamica('error', `La imagen supera el tamaño máximo permitido (${TAMANO_MAXIMO_MB} MB).`);
            inputArchivo.value = '';
            return;
        }

        // Si supera todas las verificaciones, procede a cargar la vista previa con FileReader
        const lector = new FileReader();

        lector.onload = function(e) {
            etiquetaImagen.src = e.target.result;
            etiquetaImagen.style.display = 'block';
        };

        lector.onerror = function() {
            mostrarNotificacionDinamica('error', 'Ocurrió un error al leer el archivo.');
        };

        lector.readAsDataURL(archivo);
    });
}

// =========================================================
//  FUNCIÓN DE ALERTAS Y NOTIFICACIONES DINÁMICAS
// =========================================================

/**
 * Crea y muestra de forma dinámica una notificación flotante (Toast) en el DOM.
 * Si no se encuentra el contenedor `#contenedor-notificaciones`, utiliza `alert()` como alternativa de respaldo.
 *
 * @param {'success'|'error'} tipo Tipo de notificación a desplegar ('success' o 'error').
 * @param {string} mensaje Texto o contenido descriptivo de la alerta.
 */
function mostrarNotificacionDinamica(tipo, mensaje) {
    const contenedor = document.getElementById('contenedor-notificaciones');

    // Respaldo por si falta el div objetivo en el maquetado HTML
    if (!contenedor) {
        alert(mensaje);
        return;
    }

    const toast = document.createElement('div');
    toast.className = tipo === 'success'
        ? 'libri-toast libri-toast-success'
        : 'libri-toast libri-toast-error';

    const icono = tipo === 'success'
        ? '<i class="bi bi-check-circle-fill fs-5"></i>'
        : '<i class="bi bi-exclamation-circle-fill fs-5"></i>';

    toast.innerHTML = `${icono}<span>${mensaje}</span>`;
    contenedor.appendChild(toast);

    // Animación de aparición gradual
    setTimeout(() => {
        toast.classList.add('show');
    }, 100);

    // Salida suave y eliminación del nodo en el DOM
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => {
            toast.remove();
        }, 400);
    }, 3500);
}
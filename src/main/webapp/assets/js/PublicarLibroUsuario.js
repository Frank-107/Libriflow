/**
 * @fileoverview Validador integral y gestor de publicación de publicaciones para LibriFlow.
 *
 * Controla la validación avanzada de sinopsis (mínimo 100 palabras y máximo 2900 bytes UTF-8),
 * el cálculo en tiempo real de la comisión (15%) y ganancia neta del vendedor, la verificación
 * de imágenes obligatorias (máximo 2 MB por archivo) y la gestión de notificaciones flotantes (Toasts).
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @version 1.0
 * @since 25/08/2026
 */

/**
 * Cuenta la cantidad de palabras presentes en una cadena de texto.
 *
 * @param {string} texto Cadena de texto a evaluar.
 * @returns {number} Conteo total de palabras válidas.
 */
function contarPalabras(texto) {
    if (!texto) return 0;
    const palabras = texto.trim().split(/\s+/);
    return palabras.filter(p => p.length > 0).length;
}

/**
 * Calcula el tamaño exacto en bytes de una cadena de texto codificada en UTF-8.
 *
 * @param {string} texto Cadena de texto a evaluar.
 * @returns {number} Total de bytes que ocupa la cadena.
 */
function contarBytesUTF8(texto) {
    return new TextEncoder().encode(texto).length;
}

/** Límite máximo permitido de peso en bytes UTF-8 para el campo de sinopsis */
const SINOPSIS_MAX_BYTES = 2900;

/**
 * Valida la sinopsis del libro garantizando un mínimo de 100 palabras
 * y un tamaño máximo de 2900 bytes UTF-8 mediante mensajes de validez HTML5.
 */
function validarSinopsis() {
    const inputSinopsis = document.getElementById('sinopsis');
    if (!inputSinopsis) return;

    const numPalabras = contarPalabras(inputSinopsis.value);
    const numBytes = contarBytesUTF8(inputSinopsis.value);

    if (numPalabras < 100) {
        const faltantes = 100 - numPalabras;
        inputSinopsis.setCustomValidity("La sinopsis debe tener al menos 100 palabras. Llevas " + numPalabras + " (faltan " + faltantes + ").");
    } else if (numBytes > SINOPSIS_MAX_BYTES) {
        inputSinopsis.setCustomValidity("La sinopsis es demasiado larga (" + numBytes + "/" + SINOPSIS_MAX_BYTES + " bytes). Reduce el texto.");
    } else {
        inputSinopsis.setCustomValidity("");
    }
}

/**
 * Actualiza la interfaz gráfica con el desglose de la comisión (15%)
 * y la ganancia neta (85%) estimada para el vendedor según el precio ingresado.
 */
function actualizarPrecioUI() {
    const inputPrecio = document.getElementById('precio');
    const mensaje15 = document.getElementById('mensaje15');
    if (!inputPrecio || !mensaje15) return;

    const valor = parseFloat(inputPrecio.value);
    inputPrecio.setCustomValidity("");

    if (!isNaN(valor) && valor > 0) {
        const comision = (valor * 0.15).toFixed(2);
        const ganancia = (valor * 0.85).toFixed(2);
        mensaje15.textContent = "Se aplicará el 15% de comisión ($" + comision + "). Recibirás: $" + ganancia + " MXN";
    } else {
        mensaje15.textContent = "";
    }
}

/**
 * Verifica de forma nativa que los 3 campos de imágenes obligatorios contengan archivos seleccionados.
 *
 * @returns {boolean} `true` si las 3 imágenes están presentes; de lo contrario `false`.
 */
function validarImagenesNativo() {
    const img1 = document.getElementById("imagen1");
    const img2 = document.getElementById("imagen2");
    const img3 = document.getElementById("imagen3");

    [img1, img2, img3].forEach(img => { if (img) img.setCustomValidity(""); });

    if (!img1 || !img2 || !img3 || img1.files.length === 0 || img2.files.length === 0 || img3.files.length === 0) {
        if (img1.files.length === 0) img1.setCustomValidity("Debes subir las 3 imágenes requeridas.");
        else if (img2.files.length === 0) img2.setCustomValidity("Debes subir las 3 imágenes requeridas.");
        else if (img3.files.length === 0) img3.setCustomValidity("Debes subir las 3 imágenes requeridas.");

        return false;
    }
    return true;
}

/**
 * Genera la vista previa de las imágenes del libro y valida que no superen el límite de 2 MB.
 *
 * @param {HTMLInputElement} input Elemento de entrada de tipo file.
 * @param {string} imgId Identificador del elemento `<img>` para la vista previa.
 * @param {string} placeholderId Identificador del contenedor de marcador de posición (placeholder).
 */
function previewImagenConValidacion(input, imgId, placeholderId) {
    const imgElement = document.getElementById(imgId);
    const placeholderElement = document.getElementById(placeholderId);

    if (input.files && input.files[0]) {
        const archivo = input.files[0];
        const maximoBytes = 2 * 1024 * 1024; // 2 MB

        if (archivo.size > maximoBytes) {
            mostrarNotificacionDinamica('error', 'La imagen excede el límite permitido de 2MB. Por favor, selecciona una más ligera.');

            input.value = "";
            imgElement.src = "";
            imgElement.style.display = 'none';
            if (placeholderElement) placeholderElement.style.display = 'block';
            return;
        }

        const reader = new FileReader();
        reader.onload = function(e) {
            imgElement.src = e.target.result;
            imgElement.style.display = 'block';
            if (placeholderElement) placeholderElement.style.display = 'none';
        };
        reader.readAsDataURL(archivo);
    }
}

document.addEventListener("DOMContentLoaded", function () {
    // 1. Manejo de animación de salida para avisos de éxito estáticos
    const toast = document.getElementById('toastExito');
    if (toast) {
        setTimeout(function() {
            toast.classList.add('mostrar-toast');
        }, 150);

        setTimeout(function() {
            toast.classList.remove('mostrar-toast');
            setTimeout(function() { toast.remove(); }, 500);
        }, 4500);
    }

    // 2. Notificación de errores devueltos por la capa del servidor
    const errorServidor = document.getElementById('errorServidor');
    if (errorServidor) {
        const mensaje = errorServidor.getAttribute('data-mensaje');
        if (mensaje && typeof mostrarNotificacionDinamica === 'function') {
            mostrarNotificacionDinamica('error', mensaje);
        }
    }

    // 3. Inicialización del estado visual y validaciones del formulario
    const form = document.getElementById('formPublicar');
    actualizarPrecioUI();
    validarSinopsis();

    ['imagen1', 'imagen2', 'imagen3'].forEach(id => {
        const elem = document.getElementById(id);
        if (elem) {
            elem.addEventListener('change', function () {
                this.setCustomValidity('');
            });
        }
    });

    // 4. Intercepción del evento 'submit' para validar campos antes del envío
    if (form) {
        form.addEventListener('submit', function (e) {
            const inputPrecio = document.getElementById('precio');
            const valorPrecio = parseFloat(inputPrecio.value);
            if (isNaN(valorPrecio) || valorPrecio <= 0) {
                inputPrecio.setCustomValidity("El precio debe ser mayor a $0 MXN.");
            } else {
                inputPrecio.setCustomValidity("");
            }

            validarSinopsis();

            const imagenesValidas = validarImagenesNativo();

            // Despliega el modal de subida de imágenes si la validación falla
            if (!imagenesValidas) {
                e.preventDefault();
                e.stopPropagation();

                const modalElement = document.getElementById('modalSubirImagenes');
                const bsModal = bootstrap.Modal.getOrCreateInstance(modalElement);
                bsModal.show();

                modalElement.addEventListener('shown.bs.modal', function handler() {
                    form.reportValidity();
                    modalElement.removeEventListener('shown.bs.modal', handler);
                });
                return;
            }

            // Detiene el proceso si existe algún error de validez nativa de HTML5
            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
                form.reportValidity();
                return;
            }

            // Bloquea el botón de envío para evitar peticiones duplicadas
            const btn = form.querySelector('.btn-submit');
            if (btn) {
                btn.disabled = true;
                btn.innerHTML = 'Enviando...';
            }
        });
    }
});

/**
 * Genera de forma dinámica una notificación emergente (Toast) en la pantalla.
 * Si no se encuentra el contenedor objetivo en la plantilla, hace fallback a `alert()` nativo.
 *
 * @param {'success'|'error'} tipo Categoría de la alerta ('success' o 'error').
 * @param {string} mensaje Contenido del mensaje a desplegar.
 */
function mostrarNotificacionDinamica(tipo, mensaje) {
    const contenedor = document.getElementById('contenedor-notificaciones');

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

    // Animación de entrada gradual
    setTimeout(() => {
        toast.classList.add('show');
    }, 100);

    // Salida y destrucción del elemento del DOM
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => {
            toast.remove();
        }, 400);
    }, 3500);
}
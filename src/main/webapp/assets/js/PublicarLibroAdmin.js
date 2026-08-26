/**
 * @fileoverview Validador integral y gestor de previsualización para el formulario de publicación de libros en LibriFlow.
 *
 * Gestiona el conteo dinámico de palabras para la sinopsis (mínimo 100 palabras), la validación obligatoria
 * de modalidades (Venta/Renta), el control de peso (máximo 2 MB) y previsualización de 3 imágenes del libro,
 * la notificación de errores del servidor y la generación dinámica de notificaciones flotantes (Toasts).
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
 * Valida que la sinopsis del libro cumpla con una extensión mínima de 100 palabras.
 * Actualiza el mensaje de validación personalizado de HTML5 (setCustomValidity).
 */
function validarSinopsis() {
    const inputSinopsis = document.getElementById('sinopsis');
    if (!inputSinopsis) return;

    const numPalabras = contarPalabras(inputSinopsis.value);

    if (numPalabras < 100) {
        const faltantes = 100 - numPalabras;
        inputSinopsis.setCustomValidity("La sinopsis debe tener al menos 100 palabras. Llevas " + numPalabras + " (faltan " + faltantes + ").");
    } else {
        inputSinopsis.setCustomValidity("");
    }
}

/**
 * Valida que el usuario seleccione al menos un tipo de modalidad de publicación (Venta o Renta).
 *
 * @returns {boolean} `true` si al menos una modalidad está seleccionada; de lo contrario `false`.
 */
function validarTipoPublicacion() {
    const checkVenta = document.getElementById('checkVenta');
    const checkRenta = document.getElementById('checkRenta');

    if (!checkVenta || !checkRenta) return true;

    if (!checkVenta.checked && !checkRenta.checked) {
        checkVenta.setCustomValidity("Debes seleccionar al menos un tipo de publicación (Venta o Renta).");
        return false;
    } else {
        checkVenta.setCustomValidity("");
        checkRenta.setCustomValidity("");
        return true;
    }
}

/**
 * Verifica que los 3 campos de imágenes obligatorios dentro de la ventana modal contengan un archivo cargado.
 *
 * @returns {boolean} `true` si los 3 archivos están presentes; `false` si falta alguno.
 */
function validarImagenesModal() {
    const img1 = document.getElementById("imagen1");
    const img2 = document.getElementById("imagen2");
    const img3 = document.getElementById("imagen3");

    [img1, img2, img3].forEach(img => { if (img) img.setCustomValidity(""); });

    if (!img1 || img1.files.length === 0) {
        if (img1) img1.setCustomValidity("Debes subir las 3 imágenes requeridas.");
        return false;
    }
    if (!img2 || img2.files.length === 0) {
        if (img2) img2.setCustomValidity("Debes subir las 3 imágenes requeridas.");
        return false;
    }
    if (!img3 || img3.files.length === 0) {
        if (img3) img3.setCustomValidity("Debes subir las 3 imágenes requeridas.");
        return false;
    }

    return true;
}

/**
 * Actualiza la vista previa de las imágenes del libro dentro del modal de subida.
 * Limita el tamaño de cada archivo a 2 MB y muestra notificaciones dinámicas en caso de exceso.
 *
 * @param {HTMLInputElement} input Campo de entrada de tipo file.
 * @param {string} idImg Identificador de la etiqueta `<img>` de destino para la vista previa.
 * @param {string} idPlaceholder Identificador del elemento contenedor de marcador de posición (placeholder).
 */
function actualizarVistaPreviaModal(input, idImg, idPlaceholder) {
    const img = document.getElementById(idImg);
    const placeholder = document.getElementById(idPlaceholder);

    if (input.files && input.files[0]) {
        const archivo = input.files[0];
        const maximoBytes = 2 * 1024 * 1024; // 2 MB

        // Validación de peso de imagen (Máximo 2 MB)
        if (archivo.size > maximoBytes) {
            mostrarNotificacionDinamica('error', 'La imagen excede el límite permitido de 2MB. Por favor, selecciona una más ligera.');
            input.value = ""; // Limpieza de la selección
            if (img) {
                img.src = "";
                img.style.display = 'none';
            }
            if (placeholder) {
                placeholder.style.display = 'block';
            }
            return;
        }

        // Lectura asíncrona para la vista previa
        const reader = new FileReader();
        reader.onload = function(e) {
            if (img) {
                img.src = e.target.result;
                img.style.display = 'block';
            }
            if (placeholder) {
                placeholder.style.display = 'none';
            }
        };
        reader.readAsDataURL(input.files[0]);
    }
}

document.addEventListener("DOMContentLoaded", function () {
    // 1. Manejo y animación de avisos de éxito estilo Toast estáticos
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

    // 2. Detección de mensajes de error devueltos desde el backend (Servlets/Jakarta EE)
    const errorServidor = document.getElementById('errorServidor');
    if (errorServidor) {
        const mensaje = errorServidor.getAttribute('data-mensaje');
        if (mensaje && typeof mostrarNotificacionDinamica === 'function') {
            mostrarNotificacionDinamica('error', mensaje);
        }
    }

    // 3. Registro de escuchadores de eventos sobre el tipo de publicación
    const form = document.getElementById('formPublicar');
    const checkVenta = document.getElementById('checkVenta');
    const checkRenta = document.getElementById('checkRenta');

    if (checkVenta && checkRenta) {
        checkVenta.addEventListener('change', validarTipoPublicacion);
        checkRenta.addEventListener('change', validarTipoPublicacion);
    }

    // 4. Configuración de escuchadores para la subida de las 3 imágenes requeridas
    const configuracionImagenes = [
        { id: 'imagen1', img: 'vistapreviaImg1', placeholder: 'placeholderImg1' },
        { id: 'imagen2', img: 'vistapreviaImg2', placeholder: 'placeholderImg2' },
        { id: 'imagen3', img: 'vistapreviaImg3', placeholder: 'placeholderImg3' }
    ];

    configuracionImagenes.forEach(conf => {
        const elem = document.getElementById(conf.id);
        if (elem) {
            elem.addEventListener('change', function () {
                this.setCustomValidity('');
                actualizarVistaPreviaModal(this, conf.img, conf.placeholder);
            });
        }
    });

    // Validar extensión inicial de la sinopsis
    validarSinopsis();

    // 5. Intercepción y control del envío del formulario (Submit)
    if (form) {
        form.addEventListener('submit', function (e) {
            const inputPrecio = document.getElementById('precio');
            const valorPrecio = parseFloat(inputPrecio.value);

            // Validación de precio positivo
            if (isNaN(valorPrecio) || valorPrecio <= 0) {
                inputPrecio.setCustomValidity("El precio debe ser mayor a $0 MXN.");
            } else {
                inputPrecio.setCustomValidity("");
            }

            validarTipoPublicacion();
            validarSinopsis();

            const imagenesCompletas = validarImagenesModal();

            // Si faltan imágenes, detiene el envío y despliega el modal correspondiente
            if (!imagenesCompletas) {
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

            // Si la validación global de HTML5 falla, muestra los globos nativos
            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
                form.reportValidity();
                return;
            }

            // Deshabilita el botón e indica el estado de envío en proceso
            const btn = form.querySelector('.btn-submit');
            if (btn) {
                btn.disabled = true;
                btn.innerHTML = 'Enviando...';
            }
        });
    }
});

/**
 * Crea y muestra de forma dinámica una notificación flotante (Toast) en el DOM.
 * Si no existe el contenedor `#contenedor-notificaciones`, muestra una alerta nativa del navegador.
 *
 * @param {'success'|'error'} tipo Tipo de alerta a mostrar ('success' o 'error').
 * @param {string} mensaje Texto o mensaje descriptivo de la notificación.
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

    // Animación de entrada
    setTimeout(() => {
        toast.classList.add('show');
    }, 100);

    // Animación de salida y remoción
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => {
            toast.remove();
        }, 400);
    }, 3500);
}
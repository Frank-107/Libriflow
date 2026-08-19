function contarPalabras(texto) {
    if (!texto) return 0;
    const palabras = texto.trim().split(/\s+/);
    return palabras.filter(p => p.length > 0).length;
}

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

// Manejador genérico para previsualizar imágenes en la tarjeta
function actualizarVistaPreviaModal(input, idImg, idPlaceholder) {
    const img = document.getElementById(idImg);
    const placeholder = document.getElementById(idPlaceholder);

    if (input.files && input.files[0]) {
        const archivo = input.files[0];
        const maximoBytes = 2 * 1024 * 1024; // 2 MB

        // --- SE AGREGÓ ESTA VALIDACIÓN DE PESO CON LA ALERTA DINÁMICA ---
        if (archivo.size > maximoBytes) {
            mostrarNotificacionDinamica('error', 'La imagen excede el límite permitido de 2MB. Por favor, selecciona una más ligera.');
            input.value = ""; // Limpia el input
            if (img) {
                img.src = "";
                img.style.display = 'none';
            }
            if (placeholder) {
                placeholder.style.display = 'block';
            }
            return;
        }

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

    // --- SE AGREGÓ ESTO PARA MOSTRAR ERRORES DEL SERVIDOR (COMO EN EL OTRO JS) ---
    const errorServidor = document.getElementById('errorServidor');
    if (errorServidor) {
        const mensaje = errorServidor.getAttribute('data-mensaje');
        if (mensaje && typeof mostrarNotificacionDinamica === 'function') {
            mostrarNotificacionDinamica('error', mensaje);
        }
    }

    const form = document.getElementById('formPublicar');
    const checkVenta = document.getElementById('checkVenta');
    const checkRenta = document.getElementById('checkRenta');

    if (checkVenta && checkRenta) {
        checkVenta.addEventListener('change', validarTipoPublicacion);
        checkRenta.addEventListener('change', validarTipoPublicacion);
    }

    // Listener para los 3 campos de imágenes (asigna la vista previa y remueve customValidity)
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

    validarSinopsis();

    if (form) {
        form.addEventListener('submit', function (e) {
            const inputPrecio = document.getElementById('precio');
            const valorPrecio = parseFloat(inputPrecio.value);
            if (isNaN(valorPrecio) || valorPrecio <= 0) {
                inputPrecio.setCustomValidity("El precio debe ser mayor a $0 MXN.");
            } else {
                inputPrecio.setCustomValidity("");
            }

            validarTipoPublicacion();
            validarSinopsis();

            const imagenesCompletas = validarImagenesModal();

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

            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
                form.reportValidity();
                return;
            }

            const btn = form.querySelector('.btn-submit');
            if (btn) {
                btn.disabled = true;
                btn.innerHTML = 'Enviando...';
            }
        });
    }
});

// =========================================================
//  FUNCIÓN DE ALERTAS AÑADIDA AL FINAL
// =========================================================
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
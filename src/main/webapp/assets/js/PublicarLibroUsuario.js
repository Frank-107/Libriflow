function contarPalabras(texto) {
    if (!texto) return 0;
    const palabras = texto.trim().split(/\s+/);
    return palabras.filter(p => p.length > 0).length;
}

function contarBytesUTF8(texto) {
    return new TextEncoder().encode(texto).length;
}

const SINOPSIS_MAX_BYTES = 2900;

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

function previewImagenConValidacion(input, imgId, placeholderId) {
    const imgElement = document.getElementById(imgId);
    const placeholderElement = document.getElementById(placeholderId);

    if (input.files && input.files[0]) {
        const archivo = input.files[0];
        const maximoBytes = 2 * 1024 * 1024; // 2 MB

        if (archivo.size > maximoBytes) {
            alert("La imagen excede el límite permitido de 2MB. Por favor, selecciona una más ligera.");

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
    // 1. Animación para publicación exitosa
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

    // 2. Notificación de errores del servidor (Fuera del bloque toast)
    const errorServidor = document.getElementById('errorServidor');
    if (errorServidor) {
        const mensaje = errorServidor.getAttribute('data-mensaje');
        if (mensaje && typeof mostrarNotificacionDinamica === 'function') {
            mostrarNotificacionDinamica('error', mensaje);
        }
    }

    // 3. Inicialización de formulario
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
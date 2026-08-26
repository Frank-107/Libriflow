/**
 * @fileoverview Previsualización de imágenes y control de actualización de formularios para LibriFlow.
 *
 * Proporciona validación del lado del cliente para archivos de imagen (formatos permitidos y límite de 5 MB)
 * junto con previsualización en tiempo real, e implementa el control de flujo para la confirmación de formularios
 * mediante modales de Bootstrap 5 antes del envío definitivo.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @version 1.0
 * @since 25/08/2026
 */

/**
 * Previsualiza una imagen seleccionada por el usuario en un elemento HTML <img>.
 * Realiza la validación previa de extensión (JPG, PNG, WEBP) y tamaño máximo de 5 MB.
 *
 * @param {HTMLInputElement} input Elemento de entrada de tipo file que contiene el archivo cargado.
 * @param {string} idPreview Identificador del elemento <img> destino donde se mostrará la imagen.
 */
function mostrarPreview(input, idPreview) {
    if (!input.files || !input.files[0]) {
        return;
    }

    const archivo = input.files[0];

    // Formatos MIME autorizados en la plataforma
    const permitidos = [
        "image/jpeg",
        "image/png",
        "image/webp"
    ];

    // Validación de tipo de archivo
    if (!permitidos.includes(archivo.type)) {
        alert("Selecciona una imagen JPG, PNG o WEBP.");
        input.value = "";
        return;
    }

    // Validación de peso máximo (5 MB = 5 * 1024 * 1024 bytes)
    if (archivo.size > 5 * 1024 * 1024) {
        alert("La imagen no puede superar los 5 MB.");
        input.value = "";
        return;
    }

    // Lectura asíncrona de la imagen en formato DataURL
    const reader = new FileReader();

    reader.onload = function(e) {
        document.getElementById(idPreview).src = e.target.result;
    };

    reader.readAsDataURL(archivo);
}

// --------------------------------------------------------------------------
// Gestión del flujo de confirmación y envío de la edición
// --------------------------------------------------------------------------

const formEditar = document.getElementById("formEditar");
const btnConfirmarActualizacion = document.getElementById("btnConfirmarActualizacion");
let actualizacionConfirmada = false;

if (formEditar && btnConfirmarActualizacion) {

    /**
     * Intercepta el evento de envío del formulario para requerir la confirmación previa en una modal.
     * Al confirmarse, aplica estado visual de carga ("Guardando...") y deshabilita el botón principal.
     */
    formEditar.addEventListener("submit", function(event) {

        if (!actualizacionConfirmada) {
            event.preventDefault();

            const modal = bootstrap.Modal.getOrCreateInstance(
                document.getElementById("modalConfirmarActualizacion")
            );

            modal.show();
            return;
        }

        const boton = this.querySelector(".btn-submit-editar");

        if (boton) {
            boton.disabled = true;
            boton.innerHTML =
                '<span class="spinner-border spinner-border-sm me-2"></span>Guardando...';
        }
    });

    /**
     * Confirma la edición desde la ventana modal, actualiza la bandera de validación,
     * oculta el modal y activa el envío programático (`requestSubmit`).
     */
    btnConfirmarActualizacion.addEventListener("click", function() {

        actualizacionConfirmada = true;

        const modal = bootstrap.Modal.getInstance(
            document.getElementById("modalConfirmarActualizacion")
        );

        if (modal) {
            modal.hide();
        }

        formEditar.requestSubmit();
    });
}
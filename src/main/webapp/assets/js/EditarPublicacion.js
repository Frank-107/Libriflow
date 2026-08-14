function mostrarPreview(input, idPreview) {
    if (!input.files || !input.files[0]) {
        return;
    }

    const archivo = input.files[0];

    const permitidos = [
        "image/jpeg",
        "image/png",
        "image/webp"
    ];

    if (!permitidos.includes(archivo.type)) {
        alert("Selecciona una imagen JPG, PNG o WEBP.");
        input.value = "";
        return;
    }

    if (archivo.size > 5 * 1024 * 1024) {
        alert("La imagen no puede superar los 5 MB.");
        input.value = "";
        return;
    }

    const reader = new FileReader();

    reader.onload = function(e) {
        document.getElementById(idPreview).src = e.target.result;
    };

    reader.readAsDataURL(archivo);
}

const formEditar = document.getElementById("formEditar");
const btnConfirmarActualizacion = document.getElementById("btnConfirmarActualizacion");
let actualizacionConfirmada = false;

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

    boton.disabled = true;
    boton.innerHTML =
        '<span class="spinner-border spinner-border-sm me-2"></span>Guardando...';
});

btnConfirmarActualizacion.addEventListener("click", function() {

    actualizacionConfirmada = true;

    const modal = bootstrap.Modal.getInstance(
        document.getElementById("modalConfirmarActualizacion")
    );

    modal.hide();

    formEditar.requestSubmit();
});
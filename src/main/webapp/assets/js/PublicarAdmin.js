const formulario = document.getElementById("formPublicar");
const checkVenta = document.getElementById("checkVenta");
const checkRenta = document.getElementById("checkRenta");
const inputPrecio = document.querySelector("input[name='precio']");

formulario.addEventListener("submit", validarFormulario);

function validarFormulario(e) {
    const imagen1 = document.getElementById("imagen1");
    const imagen2 = document.getElementById("imagen2");
    const imagen3 = document.getElementById("imagen3");

    if (!checkVenta.checked && !checkRenta.checked) {
        e.preventDefault();
        alert("Debes seleccionar el tipo de publicación (Venta, Renta o ambas).");
        document.querySelector('.btn-submit').disabled = false;
        document.querySelector('.btn-submit').innerHTML = 'Publicar';
        return;
    }

    if (
        imagen1.files.length === 0 ||
        imagen2.files.length === 0 ||
        imagen3.files.length === 0
    ) {
        e.preventDefault();
        alert("Debes subir las 3 imágenes requeridas en el modal.");
        document.querySelector('.btn-submit').disabled = false;
        document.querySelector('.btn-submit').innerHTML = 'Publicar';
        return;
    }
}

function evaluarPrecio() {
    if (checkVenta.checked) {
        inputPrecio.disabled = false;
        inputPrecio.required = true;
    } else {
        inputPrecio.disabled = true;
        inputPrecio.required = false;
        inputPrecio.value = "";
    }
}

checkVenta.addEventListener("change", evaluarPrecio);
checkRenta.addEventListener("change", evaluarPrecio);

evaluarPrecio();
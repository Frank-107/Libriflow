const formulario = document.getElementById("formPublicar");

formulario.addEventListener("submit",validarImagenes);

    function validarImagenes(e){

        const imagen1 = document.getElementById("imagen1");
        const imagen2 = document.getElementById("imagen2");
        const imagen3 = document.getElementById("imagen3");

        if(
            imagen1.files.length === 0 ||
            imagen2.files.length === 0 ||
            imagen3.files.length === 0
        ){

            e.preventDefault();

            alert("Debes subir las 3 imágenes.");

        }

    }
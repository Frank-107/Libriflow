document.addEventListener("DOMContentLoaded", function() {
    let cropper;
    const inputFoto = document.getElementById('inputFotoPerfil');
    const imagenParaRecortar = document.getElementById('imagenParaRecortar');
    const modalRecortarFotoElement = document.getElementById('modalRecortarFoto');

    // Verificamos que los elementos existan para evitar errores en otras páginas
    if(inputFoto && imagenParaRecortar && modalRecortarFotoElement) {
        const modalRecortar = new bootstrap.Modal(modalRecortarFotoElement);
        const previewFoto = document.getElementById('imagenPrevia');
        const fallback = document.getElementById('iconoFallback');

        // 1. Cuando el usuario selecciona una foto
        inputFoto.addEventListener('change', function(e) {
            let files = e.target.files;
            if (files && files.length > 0) {
                let reader = new FileReader();
                reader.onload = function(event) {
                    imagenParaRecortar.src = event.target.result;
                    modalRecortar.show();
                };
                reader.readAsDataURL(files[0]);
            }
        });

        modalRecortarFotoElement.addEventListener('shown.bs.modal', function () {

            cropper = new Cropper(imagenParaRecortar, {
                aspectRatio: 1,
                viewMode: 1,
                autoCropArea: 1,
                background: false
            });
        });


        modalRecortarFotoElement.addEventListener('hidden.bs.modal', function () {
            if (cropper) {
                cropper.destroy();
                cropper = null;
            }
        });


        document.getElementById('btnGuardarRecorte').addEventListener('click', function() {
            if (!cropper) return;

            cropper.getCroppedCanvas({
                width: 400,
                height: 400
            }).toBlob(function(blob) {

                let url = URL.createObjectURL(blob);
                previewFoto.src = url;
                previewFoto.style.display = 'block';
                if (fallback) fallback.style.display = 'none';

                let file = new File([blob], "avatar_recortado.jpg", { type: "image/jpeg", lastModified: new Date().getTime() });
                let container = new DataTransfer();
                container.items.add(file);
                inputFoto.files = container.files;

                modalRecortar.hide();
            }, 'image/jpeg');
        });


        document.querySelector('#modalRecortarFoto .btn-light').addEventListener('click', function(){
            inputFoto.value = "";
        });
        document.querySelector('#modalRecortarFoto .btn-close').addEventListener('click', function(){
            inputFoto.value = "";
        });
    }
});

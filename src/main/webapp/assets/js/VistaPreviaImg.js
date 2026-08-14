const TIPOS_PERMITIDOS = ['image/png', 'image/jpeg']; // jpg y jpeg comparten el mismo MIME type
const EXTENSIONES_PERMITIDAS = ['png', 'jpg', 'jpeg'];
const TAMANO_MAXIMO_MB = 2; // ajustable según tu caso
const TAMANO_MAXIMO_BYTES = TAMANO_MAXIMO_MB * 1024 * 1024;

for (let i = 1; i <= 3; i++) {
    const inputArchivo = document.getElementById('imagen' + i);
    const etiquetaImagen = document.getElementById('vistapreviaImg' + i);

    if (!inputArchivo || !etiquetaImagen) continue;

    inputArchivo.addEventListener('change', function(evento) {
        const archivo = evento.target.files[0];

        if (!archivo) {
            alert('Por favor, selecciona un archivo de imagen válido.');
            return;
        }

        // Validar extensión
        const extension = archivo.name.split('.').pop().toLowerCase();
        if (!EXTENSIONES_PERMITIDAS.includes(extension)) {
            alert('Formato no permitido. Solo se aceptan imágenes PNG, JPG o JPEG.');
            inputArchivo.value = ''; // limpiar el input
            return;
        }

        // Validar tipo MIME (doble chequeo, más confiable que la extensión)
        if (!TIPOS_PERMITIDOS.includes(archivo.type)) {
            alert('El archivo no es una imagen válida (PNG, JPG o JPEG).');
            inputArchivo.value = '';
            return;
        }

        // Validar tamaño
        if (archivo.size > TAMANO_MAXIMO_BYTES) {
            alert(`La imagen supera el tamaño máximo permitido (${TAMANO_MAXIMO_MB} MB).`);
            inputArchivo.value = '';
            return;
        }

        // Si pasa todas las validaciones, mostrar vista previa
        const lector = new FileReader();

        lector.onload = function(e) {
            etiquetaImagen.src = e.target.result;
            etiquetaImagen.style.display = 'block';
        };

        lector.onerror = function() {
            alert('Ocurrió un error al leer el archivo.');
        };

        lector.readAsDataURL(archivo);
    });
}
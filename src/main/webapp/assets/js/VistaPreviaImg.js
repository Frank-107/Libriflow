const TIPOS_PERMITIDOS = ['image/png', 'image/jpeg'];
const EXTENSIONES_PERMITIDAS = ['png', 'jpg', 'jpeg'];
const TAMANO_MAXIMO_MB = 2;
const TAMANO_MAXIMO_BYTES = TAMANO_MAXIMO_MB * 1024 * 1024;

for (let i = 1; i <= 3; i++) {
    const inputArchivo = document.getElementById('imagen' + i);
    const etiquetaImagen = document.getElementById('vistapreviaImg' + i);

    if (!inputArchivo || !etiquetaImagen) continue;

    inputArchivo.addEventListener('change', function(evento) {
        const archivo = evento.target.files[0];

        if (!archivo) {
            mostrarNotificacionDinamica('error', 'Por favor, selecciona un archivo de imagen válido.');
            return;
        }

        // 1. Validar extensión
        const extension = archivo.name.split('.').pop().toLowerCase();
        if (!EXTENSIONES_PERMITIDAS.includes(extension)) {
            mostrarNotificacionDinamica('error', 'Formato no permitido. Solo se aceptan imágenes PNG, JPG o JPEG.');
            inputArchivo.value = '';
            return;
        }

        // 2. Validar tipo MIME
        if (!TIPOS_PERMITIDOS.includes(archivo.type)) {
            mostrarNotificacionDinamica('error', 'El archivo no es una imagen válida (PNG, JPG o JPEG).');
            inputArchivo.value = '';
            return;
        }

        // 3. Validar tamaño (Límite de 2MB)
        if (archivo.size > TAMANO_MAXIMO_BYTES) {
            mostrarNotificacionDinamica('error', `La imagen supera el tamaño máximo permitido (${TAMANO_MAXIMO_MB} MB).`);
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
            mostrarNotificacionDinamica('error', 'Ocurrió un error al leer el archivo.');
        };

        lector.readAsDataURL(archivo);
    });
}

// =========================================================
//  FUNCIÓN DE ALERTAS (PUESTA AL FINAL DE ESTE MISMO ARCHIVO)
// =========================================================
function mostrarNotificacionDinamica(tipo, mensaje) {
    const contenedor = document.getElementById('contenedor-notificaciones');

    // Respaldo por si falta el div en el HTML
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
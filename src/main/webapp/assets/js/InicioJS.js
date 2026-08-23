let generoSeleccionado = 'TODOS';
let timerBusqueda = null;

document.addEventListener('DOMContentLoaded', () => {
    const inputBusqueda = document.getElementById('input-busqueda');
    const opcionesGenero = document.querySelectorAll('.btn-opcion-genero');

    if (inputBusqueda) {
        inputBusqueda.addEventListener('keyup', () => {
            clearTimeout(timerBusqueda);
            timerBusqueda = setTimeout(consultarCatalogo, 300);
        });
    }

    opcionesGenero.forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            generoSeleccionado = btn.getAttribute('data-genero');
            consultarCatalogo();
        });
    });
});

function consultarCatalogo() {
    const inputBusqueda = document.getElementById('input-busqueda');
    const q = inputBusqueda ? inputBusqueda.value : '';

    fetch(`inicio-js?q=${encodeURIComponent(q)}&genero=${encodeURIComponent(generoSeleccionado)}&ajax=true`)
        .then(res => res.json())
        .then(data => {
            actualizarListaFiltros(data.paramBusqueda, data.paramGenero);
            renderizarCatalogo(data.publicaciones, data.idUsuarioActual);
        })
        .catch(err => console.error(err));
}

function limpiarFiltros() {
    const inputBusqueda = document.getElementById('input-busqueda');
    if (inputBusqueda) inputBusqueda.value = '';

    generoSeleccionado = 'TODOS';
    consultarCatalogo();
}

function actualizarListaFiltros(q, genero) {
    const contenedor = document.getElementById('contenedor-filtros');
    if (!contenedor) return;

    contenedor.innerHTML = '';

    const tieneQ = q && q.trim().length > 0;
    const tieneG = genero && genero.trim().length > 0 && genero.toUpperCase() !== 'TODOS';

    if (!tieneQ && !tieneG) return;

    const div = document.createElement('div');
    div.className = 'd-flex align-items-center gap-2 mb-4 flex-wrap';

    const small = document.createElement('small');
    small.className = 'text-secondary fw-semibold';
    small.textContent = 'Filtros aplicados:';
    div.appendChild(small);

    if (tieneQ) {
        const spanQ = document.createElement('span');
        spanQ.className = 'badge bg-secondary-subtle text-dark border px-3 py-2 rounded-pill';
        spanQ.textContent = `Búsqueda: "${q}"`;
        div.appendChild(spanQ);
    }

    if (tieneG) {
        const spanG = document.createElement('span');
        spanG.className = 'badge bg-secondary-subtle text-dark border px-3 py-2 rounded-pill';
        spanG.textContent = `Género: ${genero}`;
        div.appendChild(spanG);
    }

    const btn = document.createElement('button');
    btn.type = 'button';
    btn.className = 'btn btn-sm btn-link text-danger text-decoration-none p-0 ms-2 fw-bold';
    btn.onclick = limpiarFiltros;
    btn.innerHTML = '<i class="bi bi-x-circle-fill me-1"></i> Limpiar filtros';
    div.appendChild(btn);

    contenedor.appendChild(div);
}

function renderizarCatalogo(publicaciones, idUsuarioActual) {
    const contenedor = document.getElementById('contenedor-catalogo');
    const plantilla = document.getElementById('plantilla-tarjeta');

    if (!contenedor || !plantilla) return;

    contenedor.innerHTML = '';
    const listaMostrable = publicaciones.filter(p => p.idPropietario !== idUsuarioActual);

    if (listaMostrable.length === 0) {
        const vacio = document.createElement('div');
        vacio.className = 'row g-4';
        vacio.innerHTML = `
            <div class="col-12">
                <div class="p-5 text-center rounded-lf-header text-secondary bg-white shadow-sm border border-2 border-dashed">
                    <i class="bi bi-journal-x display-3 text-muted mb-3 d-block"></i>
                    <h4 class="fw-bold text-dark mb-2">No se encontraron publicaciones</h4>
                    <p class="text-muted mb-3">Intenta buscar con otras palabras o selecciona un género diferente.</p>
                    <button type="button" onclick="limpiarFiltros()" class="btn btn-outline-dark rounded-pill px-4">
                        Ver todo el catálogo
                    </button>
                </div>
            </div>`;
        contenedor.appendChild(vacio);
        return;
    }

    const fila = document.createElement('div');
    fila.className = 'row g-3 publicaciones-lista';

    listaMostrable.forEach(pub => {
        const clon = plantilla.content.cloneNode(true);
        const article = clon.querySelector('.card-libro');

        if (pub.esLibriFlow) {
            article.classList.add('tiene-badge');
            clon.querySelector('.lf-badge').classList.remove('d-none');
        }

        const img = clon.querySelector('img');
        img.src = pub.imagenPrincipal;
        img.alt = `Portada de ${pub.titulo}`;

        clon.querySelector('.card-titulo').textContent = pub.titulo;
        clon.querySelector('.card-autor').textContent = `Autor: ${pub.autor}`;
        clon.querySelector('.card-genero').textContent = `Género: ${pub.genero}`;

        const precioElem = clon.querySelector('.card-precio');
        if (pub.precio === 0.0 || pub.precio === 0) {
            precioElem.innerHTML = '<span class="texto-solo-renta">Solo renta</span>';
        } else {
            precioElem.textContent = `$${pub.precio}`;
        }

        const urlDetalle = pub.esLibriFlow
            ? `detalle-publicacion-superad?idPublicacion=${pub.idPublicacion}`
            : `detalle-publicacion?idPublicacion=${pub.idPublicacion}`;

        clon.querySelector('.btn-detalles').href = urlDetalle;

        fila.appendChild(clon);
    });

    contenedor.appendChild(fila);
}
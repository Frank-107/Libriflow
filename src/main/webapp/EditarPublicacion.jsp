<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar publicación - LibriFlow</title>

    <link rel="icon"
          href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png"
          type="image/png">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/bootstrap.css">

    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/Publicar.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/EditarPublicacion.css">
</head>

<body class="p-3 p-md-4">

<div class="container-fluid max-width-xl mx-auto">

    <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5">

        <div class="d-flex align-items-center gap-2 gap-md-3">

            <button class="btn text-white d-md-none p-0 border-0 me-1"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#sidebarMenu"
                    aria-expanded="false"
                    aria-controls="sidebarMenu">
                <i class="bi bi-list" style="font-size:2rem;"></i>
            </button>

            <a href="${pageContext.request.contextPath}/detalle-publicacion?idPublicacion=${publicacion.idPublicacion}"
               class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
                <i class="bi bi-arrow-left"></i>
            </a>

            <span class="fw-bold fs-4 tracking-wide">
                Editar publicación
            </span>
        </div>

        <div class="d-flex align-items-center gap-3">

            <div class="text-end d-none d-md-block">
                <div class="fw-bold mb-0" style="font-size:.95rem;">
                    <c:out value="${sessionScope.usuario.nombre}"/>
                </div>

                <small class="text-white-50" style="font-size:.8rem;">
                    <c:out value="${sessionScope.usuario.correo}"/>
                </small>
            </div>

            <div class="dropdown">

                <div class="bg-lf-capsule rounded-circle d-flex align-items-center justify-content-center shadow-sm"
                     style="width:48px;height:48px;cursor:pointer;"
                     data-bs-toggle="dropdown"
                     aria-expanded="false">
                    <i class="bi bi-person-fill fs-4 text-dark"></i>
                </div>

                <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0 dropdown-menu-lf">
                    <li>
                        <a class="dropdown-item py-2 dropdown-lf-item"
                           href="${pageContext.request.contextPath}/actualizar-perfil">
                            <i class="bi bi-person me-2"></i>
                            Ver perfil
                        </a>
                    </li>

                    <li>
                        <a class="dropdown-item py-2 dropdown-lf-logout"
                           href="${pageContext.request.contextPath}/cerrar-sesion">
                            <i class="bi bi-box-arrow-right me-2"></i>
                            Cerrar sesión
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </header>

    <div class="row g-4">
        <aside class="col-12 col-md-4 col-lg-3">
            <div class="collapse d-md-block" id="sidebarMenu">
                <div class="bg-lf-dark p-4 rounded-lf-sidebar d-flex flex-column gap-3 shadow-sm mb-3 mb-md-0">
                    <a href="${pageContext.request.contextPath}/inicio"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-house me-3 fs-5"></i>
                        Inicio
                    </a>

                    <a href="${pageContext.request.contextPath}/carrito"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-cart3 me-3 fs-5"></i>
                        Carrito
                    </a>

                    <a href="${pageContext.request.contextPath}/mis-compras"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-bag-check me-3 fs-5"></i>
                        Compras
                    </a>

                    <a href="${pageContext.request.contextPath}/publicar-libro-usuario"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-pencil-square me-3 fs-5"></i>
                        Publicar
                    </a>

                    <a href="${pageContext.request.contextPath}/mis-publicaciones-js"
                       class="btn bg-lf-capsule btn-lf-pill sidebar-active w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-grid-3x3-gap me-3 fs-5"></i>
                        Mis publicaciones
                    </a>

                    <a href="${pageContext.request.contextPath}/mis-rentas"
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-journal-bookmark me-3 fs-5"></i>
                        Mis rentas
                    </a>

                    <a href="https://www.instagram.com/libriflow.oficial?igsh=MW9qbmNld2M2ZXNyeA=="
                       class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
                        <i class="bi bi-globe me-3 fs-5"></i>
                        Nuestras redes
                    </a>

                </div>

            </div>
        </aside>
        <main class="col-12 col-md-8 col-lg-9 catalogo-scroll">
            <div class="form-container-lf p-4 p-md-5 shadow-sm bg-white">
                <div class="d-flex justify-content-between align-items-start mb-4 flex-wrap gap-3">
                    <div>
                        <h4 class="fw-bold text-dark mb-1">
                            Editar publicación
                        </h4>

                        <p class="text-muted mb-0">
                            Actualiza la información de tu libro.
                        </p>
                    </div>

                    <img src="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png"
                         alt="LibriFlow"
                         class="logo-editar">
                </div>

                <c:if test="${not empty sessionScope.error}">
                    <div class="alert alert-danger rounded-4 mb-4">
                        <i class="bi bi-exclamation-circle me-2"></i>
                        <c:out value="${sessionScope.error}"/>
                    </div>
                    <c:remove var="error" scope="session"/>
                </c:if>

                <form action="${pageContext.request.contextPath}/editar-publicacion"
                      id="formEditar"
                      method="POST"
                      enctype="multipart/form-data">

                    <input type="hidden"
                           name="accion"
                           value="editar">

                    <input type="hidden"
                           name="idPublicacion"
                           value="${publicacion.idPublicacion}">

                    <div class="row">

                        <div class="col-12 col-md-6 mb-4">

                            <label class="form-label-lf">
                                Nombre del libro
                            </label>

                            <input type="text"
                                   name="titulo"
                                   class="form-control form-control-lf"
                                   value="<c:out value='${publicacion.titulo}'/>"
                                   required>
                        </div>

                        <div class="col-12 col-md-6 mb-4">

                            <label class="form-label-lf">
                                Autor
                            </label>

                            <input type="text"
                                   name="autor"
                                   class="form-control form-control-lf"
                                   value="<c:out value='${publicacion.autor}'/>"
                                   required>
                        </div>

                        <div class="col-12 col-md-6 mb-4">

                            <label class="form-label-lf">
                                Editorial
                            </label>

                            <input type="text"
                                   name="editorial"
                                   class="form-control form-control-lf"
                                   value="<c:out value='${publicacion.editorial}'/>"
                                   required>
                        </div>

                        <div class="col-12 col-md-6 mb-4">

                            <label class="form-label-lf">
                                Género
                            </label>

                            <select name="genero"
                                    class="form-control form-control-lf"
                                    required>

                                <option value="Novela" ${publicacion.genero == 'Novela' ? 'selected' : ''}>Novela</option>
                                <option value="Fantasía" ${publicacion.genero == 'Fantasía' ? 'selected' : ''}>Fantasía</option>
                                <option value="Ciencia ficción" ${publicacion.genero == 'Ciencia ficción' ? 'selected' : ''}>Ciencia ficción</option>
                                <option value="Terror" ${publicacion.genero == 'Terror' ? 'selected' : ''}>Terror</option>
                                <option value="Romance" ${publicacion.genero == 'Romance' ? 'selected' : ''}>Romance</option>
                                <option value="Misterio" ${publicacion.genero == 'Misterio' ? 'selected' : ''}>Misterio</option>
                                <option value="Suspenso" ${publicacion.genero == 'Suspenso' ? 'selected' : ''}>Suspenso</option>
                                <option value="Drama" ${publicacion.genero == 'Drama' ? 'selected' : ''}>Drama</option>
                                <option value="Aventura" ${publicacion.genero == 'Aventura' ? 'selected' : ''}>Aventura</option>
                                <option value="Historia" ${publicacion.genero == 'Historia' ? 'selected' : ''}>Historia</option>
                                <option value="Biografía" ${publicacion.genero == 'Biografía' ? 'selected' : ''}>Biografía</option>
                                <option value="Autobiografía" ${publicacion.genero == 'Autobiografía' ? 'selected' : ''}>Autobiografía</option>
                                <option value="Ciencia" ${publicacion.genero == 'Ciencia' ? 'selected' : ''}>Ciencia</option>
                                <option value="Tecnología" ${publicacion.genero == 'Tecnología' ? 'selected' : ''}>Tecnología</option>
                                <option value="Educación" ${publicacion.genero == 'Educación' ? 'selected' : ''}>Educación</option>
                                <option value="Infantil" ${publicacion.genero == 'Infantil' ? 'selected' : ''}>Infantil</option>
                                <option value="Poesía" ${publicacion.genero == 'Poesía' ? 'selected' : ''}>Poesía</option>
                                <option value="Filosofía" ${publicacion.genero == 'Filosofía' ? 'selected' : ''}>Filosofía</option>
                                <option value="Religión" ${publicacion.genero == 'Religión' ? 'selected' : ''}>Religión</option>
                                <option value="Cómic" ${publicacion.genero == 'Cómic' ? 'selected' : ''}>Cómic</option>
                                <option value="Otro" ${publicacion.genero == 'Otro' ? 'selected' : ''}>Otro</option>

                            </select>
                        </div>

                        <div class="col-12 col-md-6 mb-4">

                            <label class="form-label-lf">
                                Precio MXN
                            </label>

                            <input type="number"
                                   step="0.01"
                                   min="0.01"
                                   name="precio"
                                   class="form-control form-control-lf"
                                   value="${publicacion.precio}"
                                   required>
                        </div>

                        <div class="col-12 col-md-6 mb-4">

                            <label class="form-label-lf">
                                Imágenes del libro
                            </label>

                            <button type="button"
                                    class="btn btn-action-lf shadow-sm w-100"
                                    data-bs-toggle="modal"
                                    data-bs-target="#modalEditarImagenes">

                                <i class="bi bi-images me-2"></i>
                                Revisar / cambiar imágenes
                            </button>
                        </div>

                        <div class="col-12 mb-4">

                            <label class="form-label-lf">
                                Sinopsis
                            </label>

                            <textarea name="sinopsis"
                                      class="form-control form-control-lf"
                                      rows="5"
                                      required><c:out value="${publicacion.sinopsis}"/></textarea>
                        </div>

                    </div>

                    <div class="acciones-editar">

                        <a href="${pageContext.request.contextPath}/detalle-publicacion?idPublicacion=${publicacion.idPublicacion}"
                           class="btn btn-cancelar-edicion">
                            Cancelar
                        </a>

                        <button type="submit"
                                class="btn btn-action-lf shadow-sm btn-submit-editar">

                            <i class="bi bi-check2-circle me-2"></i>
                            Guardar cambios
                        </button>

                    </div>

                    <div class="modal fade"
                         id="modalEditarImagenes"
                         tabindex="-1"
                         aria-labelledby="modalEditarImagenesLabel"
                         aria-hidden="true">

                        <div class="modal-dialog modal-dialog-centered modal-lg">

                            <div class="modal-content modal-imagenes-editar">

                                <div class="modal-header bg-lf-dark text-white p-3">

                                    <h5 class="modal-title fw-bold"
                                        id="modalEditarImagenesLabel">

                                        <i class="bi bi-images me-2"></i>
                                        Imágenes de la publicación
                                    </h5>

                                    <button type="button"
                                            class="btn-close btn-close-white"
                                            data-bs-dismiss="modal"
                                            aria-label="Cerrar">
                                    </button>

                                </div>

                                <div class="modal-body p-4 bg-light">

                                    <div class="aviso-imagenes mb-4">
                                        <i class="bi bi-info-circle"></i>

                                        <span>
                                            No necesitas volver a subir las tres imágenes.
                                            Solo cambia las que necesites.
                                        </span>
                                    </div>

                                    <div class="row g-4">

                                        <div class="col-12 col-md-4">

                                            <div class="imagen-actual-card">

                                                <div class="imagen-titulo">
                                                    Portada
                                                </div>

                                                <img id="previewImagen1"
                                                     src="${publicacion.imagenPrincipal}"
                                                     alt="Portada actual">

                                                <label for="imagen1"
                                                       class="btn-cambiar-foto">
                                                    <i class="bi bi-camera me-1"></i>
                                                    Cambiar
                                                </label>

                                                <input type="file"
                                                       id="imagen1"
                                                       name="imagen1"
                                                       class="input-imagen"
                                                       accept="image/jpeg,image/png,image/webp"
                                                       onchange="mostrarPreview(this,'previewImagen1')">
                                            </div>

                                        </div>

                                        <div class="col-12 col-md-4">

                                            <div class="imagen-actual-card">

                                                <div class="imagen-titulo">
                                                    Reverso
                                                </div>

                                                <img id="previewImagen2"
                                                     src="${publicacion.imagenReverso}"
                                                     alt="Reverso actual">

                                                <label for="imagen2"
                                                       class="btn-cambiar-foto">
                                                    <i class="bi bi-camera me-1"></i>
                                                    Cambiar
                                                </label>

                                                <input type="file"
                                                       id="imagen2"
                                                       name="imagen2"
                                                       class="input-imagen"
                                                       accept="image/jpeg,image/png,image/webp"
                                                       onchange="mostrarPreview(this,'previewImagen2')">
                                            </div>
                                        </div>
                                        <div class="col-12 col-md-4">
                                            <div class="imagen-actual-card">
                                                <div class="imagen-titulo">
                                                    Estado general
                                                </div>

                                                <img id="previewImagen3"
                                                     src="${publicacion.imagenInterior}"
                                                     alt="Estado general actual">

                                                <label for="imagen3"
                                                       class="btn-cambiar-foto">
                                                    <i class="bi bi-camera me-1"></i>
                                                    Cambiar
                                                </label>

                                                <input type="file"
                                                       id="imagen3"
                                                       name="imagen3"
                                                       class="input-imagen"
                                                       accept="image/jpeg,image/png,image/webp"
                                                       onchange="mostrarPreview(this,'previewImagen3')">
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="modal-footer bg-white p-3">

                                    <button type="button"
                                            class="btn btn-action-lf shadow-sm px-4"
                                            data-bs-dismiss="modal">

                                        <i class="bi bi-check-lg me-1"></i>
                                        Listo
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>

                <div class="zona-eliminar">

                    <div class="zona-eliminar-texto">

                        <div class="zona-eliminar-icono">
                            <i class="bi bi-trash3"></i>
                        </div>

                        <div>
                            <strong>Eliminar publicación</strong>

                            <p>
                                El libro y toda la información de esta publicación serán eliminados permanentemente.
                            </p>
                        </div>
                    </div>

                    <button type="button"
                            class="btn-eliminar-publicacion"
                            data-bs-toggle="modal"
                            data-bs-target="#modalEliminarPublicacion">

                        Eliminar publicación
                    </button>
                </div>
            </div>
        </main>
    </div>
</div>

<div class="modal fade"
     id="modalEliminarPublicacion"
     tabindex="-1"
     aria-labelledby="modalEliminarPublicacionLabel"
     aria-hidden="true">

    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content modal-eliminar">
            <div class="modal-body p-4 p-md-5 text-center">
                <div class="modal-eliminar-icono">
                    <i class="bi bi-exclamation-triangle"></i>
                </div>
                <h4 class="fw-bold mt-3"
                    id="modalEliminarPublicacionLabel">
                    ¿Eliminar publicación?
                </h4>

                <p class="text-muted">
                    Estás a punto de eliminar
                    <strong>
                        <c:out value="${publicacion.titulo}"/>
                    </strong>.
                    Esta acción no se puede deshacer.
                </p>

                <div class="d-flex gap-2 mt-4">

                    <button type="button"
                            class="btn btn-cancelar-eliminar flex-grow-1"
                            data-bs-dismiss="modal">
                        Cancelar
                    </button>

                    <form action="${pageContext.request.contextPath}/editar-publicacion"
                          method="POST"
                          class="flex-grow-1">

                        <input type="hidden"
                               name="accion"
                               value="eliminar">

                        <input type="hidden"
                               name="idPublicacion"
                               value="${publicacion.idPublicacion}">

                        <button type="submit"
                                class="btn btn-confirmar-eliminar w-100">

                            <i class="bi bi-trash3 me-1"></i>
                            Sí, eliminar
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>

<script>
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
    document.getElementById("formEditar").addEventListener("submit", function() {
        const boton = this.querySelector(".btn-submit-editar");

        boton.disabled = true;
        boton.innerHTML =
            '<span class="spinner-border spinner-border-sm me-2"></span>Guardando...';
    });
</script>
</body>
</html>

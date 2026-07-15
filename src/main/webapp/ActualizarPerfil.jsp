<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Mi Perfil - LibriFlow</title>

  <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css" />
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

  <link href="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.5.13/cropper.min.css" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
</head>
<body class="p-3 p-md-4">

<div class="container-fluid max-width-xl mx-auto">

  <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5">
    <div class="d-flex align-items-center gap-3">
      <a href="Inicio.jsp" class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
        <i class="bi bi-arrow-left"></i>
      </a>
      <span class="fw-bold fs-5 tracking-wide">Perfil de Usuario</span>
    </div>

    <div class="d-flex align-items-center gap-3">
      <div class="text-end d-none d-md-block">
        <div class="fw-bold mb-0" style="font-size: 0.95rem;">${sessionScope.usuario.nombre}</div>
        <small class="text-white-50" style="font-size: 0.8rem;">${sessionScope.usuario.correo}</small>
      </div>

      <div class="dropdown">
        <div class="bg-lf-capsule rounded-circle d-flex align-items-center justify-content-center shadow-sm" data-bs-toggle="dropdown" aria-expanded="false" style="cursor: pointer;">
          <i class="bi bi-person fs-4 text-dark"></i>
        </div>

        <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0 dropdown-menu-lf">
          <li>
            <a class="dropdown-item py-2 dropdown-lf-logout" href="logout">
              <i class="bi bi-box-arrow-right me-2"></i>Cerrar sesión
            </a>
          </li>
        </ul>
      </div>
    </div>
  </header>

  <div class="row g-4">

    <aside class="col-12 col-md-4 col-lg-3">
      <div class="bg-lf-dark p-4 rounded-lf-sidebar d-flex flex-column gap-3 shadow-sm">
        <a href="Inicio.jsp" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
          <i class="bi bi-house me-3 fs-5"></i> Inicio
        </a>
        <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
          <i class="bi bi-cart3 me-3 fs-5"></i> Carrito
        </a>
        <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
          <i class="bi bi-bag-check me-3 fs-5"></i> Compras
        </a>
        <a href="publicar-libro-usuario" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
          <i class="bi bi-pencil-square me-3 fs-5"></i> Publicar
        </a>
        <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
          <i class="bi bi-grid-3x3-gap me-3 fs-5"></i> Mis publicaciones
        </a>
        <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
          <i class="bi bi-book me-3 fs-5"></i> Mis rentas
        </a>
        <a href="#" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
          <i class="bi bi-globe me-3 fs-5"></i> Nuestras redes
        </a>
      </div>
    </aside>

    <main class="col-12 col-md-8 col-lg-9 d-flex justify-content-center align-items-start">

      <div class="perfil-card-container mt-2">

        <form action="actualizar-perfil" id="formActualizarPerfil" method="POST" enctype="multipart/form-data"
              onsubmit="if(confirm('¿Estás seguro de que deseas actualizar tus datos?')) { let btn = this.querySelector('button[type=submit]'); btn.disabled=true; btn.innerHTML='<i class=\'bi bi-hourglass-split me-2\'></i>Actualizando...'; return true; } return false;">

          <div class="text-center mb-4">
            <div class="position-relative d-inline-block mt-2" style="cursor: pointer;"
                 onclick="document.getElementById('inputFotoPerfil').click();"
                 onmouseenter="document.getElementById('cameraOverlay').style.opacity='1'"
                 onmouseleave="document.getElementById('cameraOverlay').style.opacity='0'">

              <div class="perfil-avatar mx-auto position-relative shadow-sm">
                <i id="iconoFallback" class="bi bi-person-fill text-white position-absolute" style="font-size: 4.5rem; top: 12px; z-index: 1;"></i>

                <img id="imagenPrevia"
                     src="${not empty sessionScope.usuario.foto ? sessionScope.usuario.foto : ''}"
                     onload="this.style.display='block'; document.getElementById('iconoFallback').style.display='none';"
                     style="width: 100%; height: 100%; object-fit: cover; z-index: 2; position: relative; display: ${not empty sessionScope.usuario.foto ? 'block' : 'none'};"
                     alt="Avatar" />

                <div id="cameraOverlay" class="position-absolute top-0 start-0 w-100 h-100 d-flex justify-content-center align-items-center"
                     style="background-color: rgba(0,0,0,0.4); z-index: 3; opacity: 0; transition: opacity 0.3s ease; border-radius: 50%;">
                  <i class="bi bi-camera-fill text-white" style="font-size: 2rem;"></i>
                </div>
              </div>
            </div>

            <input type="file" name="fotoPerfil" id="inputFotoPerfil" class="d-none" accept="image/png, image/jpeg">
            <small class="text-muted d-block mt-2" style="font-size: 0.75rem;">Haz clic para cambiar tu foto</small>
          </div>

          <div class="d-flex justify-content-evenly align-items-center mb-4 py-3 rounded-4 bg-white shadow-sm border border-light mx-auto" style="max-width: 550px;">
            <div class="text-center px-2">
              <span class="d-block fs-5 fw-bold text-dark">0</span>
              <span class="text-muted" style="font-size: 0.8rem;">Publicaciones</span>
            </div>
            <div class="vr bg-secondary opacity-25"></div>
            <div class="text-center px-2">
              <span class="d-block fs-5 fw-bold text-dark">0</span>
              <span class="text-muted" style="font-size: 0.8rem;">Vendidos</span>
            </div>
            <div class="vr bg-secondary opacity-25"></div>
            <div class="text-center px-2">
              <span class="d-block fs-5 fw-bold text-dark">0</span>
              <span class="text-muted" style="font-size: 0.8rem;">En Renta</span>
            </div>
            <div class="vr bg-secondary opacity-25"></div>
            <div class="text-center px-2">
              <span class="d-block fs-5 fw-bold text-danger">0</span>
              <span class="text-muted" style="font-size: 0.8rem;">Retrasos</span>
            </div>
          </div>

          <h6 class="fw-bold mb-3 mt-4" style="color: #4A4641;"><i class="bi bi-person-vcard me-2"></i>Datos Personales</h6>
          <div class="row g-3 mb-4">
            <div class="col-12">
              <label class="form-label-lf text-muted" style="font-size: 0.85rem;">Nombre(s)</label>
              <input type="text" name="nombre" value="${usuario.getNombre() != null ? usuario.getNombre() : 'Alejandro'}" class="form-control form-control-lf" required>
            </div>
            <div class="col-md-6">
              <label class="form-label-lf text-muted" style="font-size: 0.85rem;">Apellido Paterno</label>
              <input type="text" name="apellidoPaterno" value="${usuario.getApellidoPaterno() != null ? usuario.getApellidoPaterno() : 'Mena'}" class="form-control form-control-lf" required>
            </div>
            <div class="col-md-6">
              <label class="form-label-lf text-muted" style="font-size: 0.85rem;">Apellido Materno</label>
              <input type="text" name="apellidoMaterno" value="${usuario.getApellidoMaterno() != null ? usuario.getApellidoMaterno() : 'Pereyda'}" class="form-control form-control-lf">
            </div>
          </div>

          <hr class="text-muted opacity-25 mb-4">

          <h6 class="fw-bold mb-3" style="color: #4A4641;"><i class="bi bi-envelope-at me-2"></i>Contacto</h6>
          <div class="row g-3 mb-4">
            <div class="col-md-6">
              <label class="form-label-lf text-muted" style="font-size: 0.85rem;">Correo electrónico</label>
              <input type="email" name="correo" value="${usuario.getCorreo() != null ? usuario.getCorreo() : '20252ds082@utez.edu.mx'}" class="form-control form-control-lf" required>
            </div>
            <div class="col-md-6">
              <label class="form-label-lf text-muted" style="font-size: 0.85rem;">Teléfono</label>
              <input type="tel" name="telefono" value="${usuario.getTelefono() != null ? usuario.getTelefono() : '7771859680'}" class="form-control form-control-lf">
            </div>
          </div>

          <hr class="text-muted opacity-25 mb-4">

          <h6 class="fw-bold mb-3" style="color: #4A4641;"><i class="bi bi-shield-lock me-2"></i>Seguridad de la Cuenta</h6>
          <div class="row g-3 mb-4">
            <div class="col-md-6">
              <label class="form-label-lf text-muted" style="font-size: 0.85rem;">Nueva contraseña</label>
              <input type="password" name="nueva_contrasena" class="form-control form-control-lf" placeholder="Dejar en blanco para conservar actual">
            </div>
            <div class="col-md-6">
              <label class="form-label-lf text-muted" style="font-size: 0.85rem;">Confirmar contraseña</label>
              <input type="password" name="confirmar_contrasena" class="form-control form-control-lf" placeholder="Repite la nueva contraseña">
            </div>
          </div>

          <button type="submit" class="btn btn-action-lf shadow-sm w-100 py-2.5 fs-6 mt-3">
            Actualizar
          </button>

        </form>
      </div>
    </main>
  </div>
</div>

<div class="modal fade" id="modalRecortarFoto" tabindex="-1" aria-labelledby="modalRecortarLabel" aria-hidden="true" data-bs-backdrop="static">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content" style="border-radius: 15px; overflow: hidden;">
      <div class="modal-header bg-lf-dark text-white p-3">
        <h5 class="modal-title fw-bold" id="modalRecortarLabel">
          <i class="bi bi-crop me-2"></i> Ajustar Foto de Perfil
        </h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body p-3">
        <div class="img-container-cropper rounded border shadow-sm">
          <img id="imagenParaRecortar" src="">
        </div>
        <p class="text-muted small text-center mt-3 mb-0">Arrastra para mover o usa la rueda del ratón para hacer zoom.</p>
      </div>
      <div class="modal-footer bg-light p-2 border-top-0 d-flex justify-content-end">
        <button type="button" class="btn btn-light shadow-sm" data-bs-dismiss="modal">Cancelar</button>
        <button type="button" class="btn btn-action-lf shadow-sm px-4" id="btnGuardarRecorte">Listo</button>
      </div>
    </div>
  </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.5.13/cropper.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/Foto.js"></script>

</body>
</html>

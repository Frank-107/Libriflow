<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Mi Perfil - LibriFlow</title>

  <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css" />
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
</head>
<body class="p-3 p-md-4">

<div class="container-fluid max-width-xl mx-auto">

  <header class="bg-lf-dark text-white p-3 mb-4 rounded-lf-header shadow-sm d-flex justify-content-between align-items-center px-4 px-md-5">
    <div class="d-flex align-items-center gap-3">
      <a href="inicio" class="text-white text-decoration-none fs-4 btn-lf-pill p-2 d-inline-flex align-items-center justify-content-center">
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
          <i class="bi bi-person-fill fs-4 text-dark"></i>
        </div>

        <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0 dropdown-menu-lf">
          <li>
            <a class="dropdown-item py-2 dropdown-lf-logout" href="cerrar-sesion">
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
        <a href="inicio" class="btn bg-lf-capsule btn-lf-pill w-100 py-2.5 text-start d-flex align-items-center px-4">
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

        <c:if test="${not empty error}">
          <div class="libri-toast libri-toast-error">
            <i class="bi bi-exclamation-circle-fill fs-5"></i>
            <span><c:out value="${error}" escapeXml="true" /></span>
          </div>
        </c:if>

        <c:if test="${not empty exito}">
          <div class="libri-toast libri-toast-success">
            <i class="bi bi-check-circle-fill fs-5"></i>
            <span><c:out value="${exito}" escapeXml="true" /></span>
          </div>
        </c:if>


        <form action="actualizar-perfil" id="formActualizarPerfil" method="POST">
          <div class="text-center mb-4">
            <div class="position-relative d-inline-block mt-2">
              <div class="perfil-avatar mx-auto shadow-sm" style="cursor: default;">
                <i class="bi bi-person-fill text-white" style="font-size: 4.5rem; margin-top: 5px;"></i>
              </div>
            </div>
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
            <div class="col-12" style="grid-column: span 2;">
              <label class="form-label-lf text-muted" style="font-size: 0.85rem;">Nombre(s)</label>
              <input type="text" name="nombre" value="${usuario.getNombre() != null ? usuario.getNombre() : ''}" class="form-control form-control-lf" required>
            </div>
            <div class="col-12 col-md-6">
              <label class="form-label-lf text-muted" style="font-size: 0.85rem;">Apellido Paterno</label>
              <input type="text" name="apellidoPaterno" value="${usuario.getApellidoPaterno() != null ? usuario.getApellidoPaterno() : ''}" class="form-control form-control-lf" required>
            </div>
            <div class="col-12 col-md-6">
              <label class="form-label-lf text-muted" style="font-size: 0.85rem;">Apellido Materno</label>
              <input type="text" name="apellidoMaterno" value="${usuario.getApellidoMaterno() != null ? usuario.getApellidoMaterno() : ''}" class="form-control form-control-lf">
            </div>
          </div>

          <hr class="text-muted opacity-25 mb-4">

          <h6 class="fw-bold mb-3" style="color: #4A4641;"><i class="bi bi-envelope-at me-2"></i>Contacto</h6>
          <div class="row g-3 mb-4">
            <div class="col-md-6">
              <label class="form-label-lf text-muted" style="font-size: 0.85rem;">Correo electrónico</label>
              <input type="email" name="correo" value="${usuario.getCorreo() != null ? usuario.getCorreo() : ''}" class="form-control form-control-lf bg-light text-muted" readonly>
            </div>
            <div class="col-md-6">
              <label class="form-label-lf text-muted" style="font-size: 0.85rem;">Teléfono</label>
              <input type="number" name="telefono" value="${usuario.getTelefono() != null ? usuario.getTelefono() : ''}" class="form-control form-control-lf">
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

          <button type="submit" id="btnActualizarForm" class="btn btn-action-lf btn-login-style shadow-sm w-100 py-2.5 fs-6 mt-3" disabled>
            Actualizar
          </button>

        </form>
      </div>
    </main>
  </div>
</div>

<div class="modal fade" id="modalConfirmarActualizacion" tabindex="-1" aria-labelledby="modalConfirmarLabel" aria-hidden="true" data-bs-backdrop="static">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content shadow-lg" style="border-radius: 40px; background-color: #e3ded7; color: #4A4641; border: none;">

      <div class="modal-header border-0 pb-0 position-relative">
        <h5 class="modal-title fw-bold w-100 text-center" id="modalConfirmarLabel" style="color: #4A4641;">
          <i class="bi bi-question-circle me-2" style="color: #7A746E;"></i> Confirmar cambios
        </h5>
        <button type="button" class="btn-close position-absolute end-0 me-4 mt-2" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <div class="modal-body py-4 text-center">
        <p class="mb-0 fs-6 fw-medium" style="color: #6e6762;">¿Estás seguro de que deseas actualizar los datos de tu perfil?</p>
      </div>

      <div class="modal-footer border-0 pt-0 d-flex justify-content-center gap-3">
        <button type="button" class="btn bg-lf-capsule btn-lf-pill" data-bs-dismiss="modal" style="font-weight: 600; padding: 12px 30px; border-radius: 25px;">
          Cancelar
        </button>

        <button type="button" class="btn btn-action-lf btn-login-style" id="btnConfirmarSubmit" style="font-weight: 600; padding: 12px 30px; border-radius: 25px; margin: 0;">
          Sí, actualizar
        </button>
      </div>

    </div>
  </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/ActualizarPerfil.js"></script>
<script src="assets/js/Notificacion.js"></script>
</body>
</html>
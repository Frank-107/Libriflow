<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Publicación Exitosa - LibriFlow</title>

    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/LogoLibriflow.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Publicar.css?v=1.1" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Confirmacion.css?v=1.0" />
</head>
<body>
<div class="overlay-blur" id="pamphletOverlay">

    <div class="pamphlet-card text-center">

        <a href="${pageContext.request.contextPath}/inicio" class="btn-close-pamphlet" title="Cerrar notificación" aria-label="Cerrar">
            <i class="bi bi-x-lg"></i>
        </a>

        <div class="icon-badge-success shadow-sm">
            <i class="bi bi-check-lg"></i>
        </div>

        <h4 class="fw-bold text-dark mb-1">Tu libro ha sido registrado crrectamente</h4>
        <p class="text-muted small mb-4">Ahora sigue estos pasos para completar la entrega de tu libro:</p>

        <div class="pamphlet-instructions text-start">

            <div class="d-flex align-items-start gap-3">
                <span class="step-number">1</span>
                <div>
                    <span class="fw-bold text-dark d-block" style="font-size: 0.95rem;">Llevar el libro a la librería de la UTEZ</span>
                    <small class="text-secondary">Ubicada justo al frente de la cafetería <strong>"El Balcón"</strong>.</small>
                </div>
            </div>

            <hr class="pamphlet-divider">

            <div class="d-flex align-items-start gap-3">
                <span class="step-number">2</span>
                <div>
                    <span class="fw-bold text-dark d-block" style="font-size: 0.95rem;">Llevar una identificación</span>
                    <small class="text-secondary">Presenta tu credencial de la UTEZ o una identificación oficial al momento de entregar.</small>
                </div>
            </div>

        </div>

        <a href="${pageContext.request.contextPath}/inicio" class="btn btn-action-lf shadow-sm w-100 py-2.5 fw-semibold">
            Entendido
        </a>

    </div>
</div>

</body>
</html>
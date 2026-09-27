<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Error</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />
    <jsp:include page="/WEB-INF/views/comunes/mensajes.jsp" />

    <div class="page-head">
        <h2>Ocurrió un error</h2>
        <div class="page-head-acciones">
            <a class="boton boton-secundario" href="javascript:history.back()">Volver</a>
        </div>
    </div>

    <p class="muted">No se pudo completar la operación solicitada.</p>

</body>
</html>
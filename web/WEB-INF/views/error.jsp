<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Error</title>
</head>
<body>

    <h2>Ocurrió un error</h2>

    <p>
        <c:choose>
            <c:when test="${not empty error}">
                ${error}
            </c:when>
            <c:otherwise>
                No se pudo completar la operación solicitada.
            </c:otherwise>
        </c:choose>
    </p>

    <p>
        <a href="javascript:history.back()">Volver atrás</a>
    </p>

</body>
</html>
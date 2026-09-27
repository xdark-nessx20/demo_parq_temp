<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar operador</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <h2>Editar operador</h2>

    <c:if test="${not empty error}">
        <p class="mensaje mensaje-error">${error}</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/operadores" method="post">
        <input type="hidden" name="accion" value="editar" />
        <input type="hidden" name="cedula" value="${operador.cedula}" />
        <div>
            <label>Cédula (no modificable):</label>
            <input type="text" value="${operador.cedula}" disabled />
        </div>
        <div>
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" value="${operador.nombre}" required />
        </div>
        <button type="submit">Guardar cambios</button>
    </form>

    <p><a href="${pageContext.request.contextPath}/operadores">Volver al listado</a></p>
</body>
</html>

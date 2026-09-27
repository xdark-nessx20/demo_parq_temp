<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar tipo de vehículo</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <h2>Editar tipo de vehículo</h2>

    <c:if test="${not empty error}">
        <p class="mensaje mensaje-error">${error}</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/tipos-vehiculo" method="post">
        <input type="hidden" name="accion" value="editar" />
        <input type="hidden" name="id" value="${tipo.id}" />
        <div>
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" value="${tipo.nombre}" required />
        </div>
        <div>
            <label for="descripcion">Descripción:</label>
            <input type="text" id="descripcion" name="descripcion" value="${tipo.descripcion}" />
        </div>
        <button type="submit">Guardar cambios</button>
    </form>

    <p><a href="${pageContext.request.contextPath}/tipos-vehiculo">Volver al listado</a></p>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar vehículo</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <div class="page-head">
        <h2>Editar vehículo</h2>
        <div class="page-head-acciones">
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/vehiculos">← Volver</a>
        </div>
    </div>

    <c:if test="${not empty error}">
        <p class="mensaje mensaje-error">${error}</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/vehiculos" method="post">
        <input type="hidden" name="accion" value="editar" />
        <input type="hidden" name="placa" value="${vehiculo.placa}" />
        <div>
            <label>Placa (no modificable):</label>
            <input type="text" value="${vehiculo.placa}" disabled />
        </div>
        <div>
            <label for="tipoVehiculo">Tipo de vehículo:</label>
            <select id="tipoVehiculo" name="tipoVehiculo" required>
                <c:forEach var="t" items="${tipos}">
                    <option value="${t.nombre}" ${vehiculo.tipo.nombre == t.nombre ? 'selected' : ''}>${t.nombre}</option>
                </c:forEach>
            </select>
        </div>
        <button type="submit">Guardar cambios</button>
    </form>
</body>
</html>

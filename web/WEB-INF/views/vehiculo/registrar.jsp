<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar vehículo</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <h2>Registrar vehículo</h2>

    <c:if test="${not empty error}">
        <p style="color:red;"><strong>${error}</strong></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/vehiculos" method="post">

        <div>
            <label for="placa">Placa:</label>
            <input type="text" id="placa" name="placa"
                   value="${param.placa}" required />
        </div>

        <div>
            <label for="cedulaCliente">Propietario (opcional):</label>
            <select id="cedulaCliente" name="cedulaCliente">
                <option value="">-- Sin dueño --</option>
                <c:forEach var="cl" items="${clientes}">
                    <option value="${cl.cedula}">${cl.nombre} (${cl.cedula})</option>
                </c:forEach>
            </select>
        </div>

        <div>
            <label for="tipoVehiculo">Tipo de vehículo:</label>
            <select id="tipoVehiculo" name="tipoVehiculo" required>
                <option value="">-- Seleccione un tipo --</option>
                <c:forEach var="t" items="${tipos}">
                    <option value="${t.nombre}">${t.nombre}</option>
                </c:forEach>
            </select>
        </div>

        <button type="submit">Registrar</button>
    </form>

    <p><a href="${pageContext.request.contextPath}/vehiculos">Volver a vehículos</a></p>

</body>
</html>
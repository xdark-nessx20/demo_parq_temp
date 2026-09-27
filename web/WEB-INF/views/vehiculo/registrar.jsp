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

    <div class="page-head">
        <h2>Registrar vehículo</h2>
        <div class="page-head-acciones">
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/vehiculos">← Volver</a>
        </div>
    </div>

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
            <label for="cedulaCliente">Propietario:</label>
            <select id="cedulaCliente" name="cedulaCliente" required>
                <option value="">-- Seleccione un cliente --</option>
                <c:forEach var="cl" items="${clientes}">
                    <option value="${cl.cedula}" ${param.cedulaCliente == cl.cedula ? 'selected' : ''}>${cl.nombre} (${cl.cedula})</option>
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
</body>
</html>
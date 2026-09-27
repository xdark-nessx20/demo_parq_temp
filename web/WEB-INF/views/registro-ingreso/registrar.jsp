<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar ingreso</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <div class="page-head">
        <h2>Registrar ingreso de vehículo</h2>
        <div class="page-head-acciones">
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/registros-ingreso">← Volver</a>
        </div>
    </div>

    <c:if test="${not empty error}">
        <p style="color:red;"><strong>${error}</strong></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/registros-ingreso" method="post">

        <div>
            <label for="placa">Placa:</label>
            <input type="text" id="placa" name="placa" value="${param.placa}"
                   placeholder="ABC-123 (carro) · ABC-12A (moto)" required />
        </div>

        <div>
            <label for="tipoVehiculo">Tipo de vehículo:</label>
            <select id="tipoVehiculo" name="tipoVehiculo" required>
                <option value="">-- Seleccione un tipo --</option>
                <c:forEach var="t" items="${tipos}">
                    <option value="${t.nombre}" ${param.tipoVehiculo == t.nombre ? 'selected' : ''}>${t.nombre}</option>
                </c:forEach>
            </select>
        </div>

        <div>
            <label for="cedulaCliente">Propietario (cliente):</label>
            <select id="cedulaCliente" name="cedulaCliente">
                <option value="">-- Seleccione el propietario --</option>
                <c:forEach var="cl" items="${clientes}">
                    <option value="${cl.cedula}" ${param.cedulaCliente == cl.cedula ? 'selected' : ''}>${cl.nombre} (${cl.cedula})</option>
                </c:forEach>
            </select>
            <small class="muted">Solo se usa si el vehículo es nuevo; una placa ya registrada conserva su dueño.</small>
        </div>

        <button type="submit">Registrar entrada</button>
    </form>

</body>
</html>
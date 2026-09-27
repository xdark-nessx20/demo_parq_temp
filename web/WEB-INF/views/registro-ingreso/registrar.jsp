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
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/registros-ingreso">Volver</a>
        </div>
    </div>

<jsp:include page="/WEB-INF/views/comunes/mensajes.jsp" />

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

        <p class="muted">
            Si la placa es nueva, el vehículo entra <strong>sin dueño</strong>. El cliente debe
            registrarse en la app y <strong>reclamar</strong> su placa (Mi cuenta → Reclamar vehículo).
        </p>

        <button type="submit">Registrar entrada</button>
    </form>

</body>
</html>
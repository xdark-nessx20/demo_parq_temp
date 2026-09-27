<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar tarifa</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <div class="page-head">
        <h2>Registrar nueva tarifa</h2>
        <div class="page-head-acciones">
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/tarifas">← Volver</a>
        </div>
    </div>

    <c:if test="${not empty error}">
        <p style="color:red;"><strong>${error}</strong></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/tarifas" method="post">
        <input type="hidden" name="accion" value="registrar" />

        <div>
            <label for="idTipoVehiculo">Tipo de vehículo:</label>
            <select id="idTipoVehiculo" name="idTipoVehiculo" required>
                <option value="">-- Seleccione un tipo --</option>
                <c:forEach var="t" items="${tipos}">
                    <option value="${t.id}">${t.nombre}</option>
                </c:forEach>
            </select>
        </div>

        <div>
            <label for="valorHora">Valor por hora:</label>
            <input type="text" id="valorHora" name="valorHora"
                   value="${param.valorHora}" required />
        </div>

        <div>
            <label for="anioVigencia">Año de vigencia:</label>
            <input type="number" id="anioVigencia" name="anioVigencia"
                   value="${empty param.anioVigencia ? anioActual : param.anioVigencia}" required />
        </div>

        <button type="submit">Registrar</button>
    </form>

</body>
</html>
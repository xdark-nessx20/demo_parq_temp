<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar salida</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <div class="page-head">
        <h2>Registrar salida de vehículo</h2>
        <div class="page-head-acciones">
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/pagos">Volver</a>
        </div>
    </div>

<jsp:include page="/WEB-INF/views/comunes/mensajes.jsp" />

    <form action="${pageContext.request.contextPath}/pagos" method="post">

        <div>
            <label for="idRegistro">Vehículo (ticket abierto):</label>
            <select id="idRegistro" name="idRegistro" required>
                <option value="">-- Seleccione un vehículo --</option>
                <c:forEach var="r" items="${tickets}">
                    <option value="${r.id}">${empty placasPorRegistro[r.id] ? 'Vehículo sin placa' : placasPorRegistro[r.id]} — entrada ${r.horaEntradaTexto}</option>
                </c:forEach>
            </select>
        </div>

        <button type="submit">Registrar salida</button>
    </form>

</body>
</html>
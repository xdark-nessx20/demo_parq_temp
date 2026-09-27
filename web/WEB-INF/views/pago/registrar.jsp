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

    <h2>Registrar salida de vehículo</h2>

    <c:if test="${not empty error}">
        <p style="color:red;"><strong>${error}</strong></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/pagos" method="post">

        <div>
            <label for="idRegistro">Ticket:</label>
            <select id="idRegistro" name="idRegistro" required>
                <option value="">-- Seleccione un ticket abierto --</option>
                <c:forEach var="r" items="${tickets}">
                    <option value="${r.id}">Ticket ${r.id} (entrada ${r.horaEntrada})</option>
                </c:forEach>
            </select>
        </div>

        <div>
            <label for="idOperador">Operador:</label>
            <select id="idOperador" name="idOperador" required>
                <option value="">-- Seleccione un operador --</option>
                <c:forEach var="o" items="${operadores}">
                    <option value="${o.id}">${o.nombre} (${o.cedula})</option>
                </c:forEach>
            </select>
        </div>

        <button type="submit">Registrar salida</button>
    </form>

    <p><a href="${pageContext.request.contextPath}/pagos">Volver a pagos</a></p>

</body>
</html>
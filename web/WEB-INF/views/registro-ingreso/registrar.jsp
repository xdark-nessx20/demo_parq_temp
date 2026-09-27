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

    <h2>Registrar ingreso de vehículo</h2>

    <c:if test="${not empty error}">
        <p style="color:red;"><strong>${error}</strong></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/registros-ingreso" method="post">

        <div>
            <label for="idVehiculo">Vehículo:</label>
            <select id="idVehiculo" name="idVehiculo" required>
                <option value="">-- Seleccione un vehículo --</option>
                <c:forEach var="v" items="${vehiculos}">
                    <option value="${v.id}">${v.placa} (${v.tipo.nombre})</option>
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

        <button type="submit">Registrar entrada</button>
    </form>

    <p><a href="${pageContext.request.contextPath}/registros-ingreso">Volver a registros</a></p>

</body>
</html>
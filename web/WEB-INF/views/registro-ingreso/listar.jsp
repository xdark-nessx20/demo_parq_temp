<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registros de ingreso</title>
</head>
<body>

    <h2>Registros de ingreso</h2>

    <p>
        <a href="${pageContext.request.contextPath}/registros-ingreso?accion=registrar">Registrar ingreso</a>
        &nbsp;|&nbsp;
        <a href="${pageContext.request.contextPath}/pagos?accion=registrar">Registrar salida</a>
    </p>

    <c:choose>
        <c:when test="${empty ingresos}">
            <p>No hay registros de ingreso.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="6">
                <thead>
                    <tr>
                        <th>ID ticket</th>
                        <th>ID vehículo</th>
                        <th>Hora entrada</th>
                        <th>Hora salida</th>
                        <th>Operador entrada</th>
                        <th>Operador salida</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="r" items="${ingresos}">
                        <tr>
                            <td>${r.id}</td>
                            <td>${r.idVehiculo}</td>
                            <td>${r.horaEntrada}</td>
                            <td>${r.horaSalida}</td>
                            <td>${r.idOperadorEntrada}</td>
                            <td>${r.idOperadorSalida}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

</body>
</html>
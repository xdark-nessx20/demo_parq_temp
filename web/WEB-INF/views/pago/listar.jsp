<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Pagos</title>
</head>
<body>

    <h2>Pagos registrados</h2>

    <p>
        <a href="${pageContext.request.contextPath}/pagos?accion=registrar">Registrar salida y pago</a>
    </p>

    <c:choose>
        <c:when test="${empty pagos}">
            <p>No hay pagos registrados.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="6">
                <thead>
                    <tr>
                        <th>ID pago</th>
                        <th>ID ticket</th>
                        <th>Valor</th>
                        <th>Fecha de pago</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${pagos}">
                        <tr>
                            <td>${p.id}</td>
                            <td>${p.idRegistroIngreso}</td>
                            <td>$${p.valor}</td>
                            <td>${p.fechaPago}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

</body>
</html>
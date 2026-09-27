<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Pago</title>
</head>
<body>

    <h2>Pago registrado</h2>

    <table border="1" cellpadding="6">
        <tr><th>ID pago</th><td>${pago.id}</td></tr>
        <tr><th>ID ticket</th><td>${pago.idRegistroIngreso}</td></tr>
        <tr><th>Valor</th><td>$${pago.valor}</td></tr>
        <tr><th>Fecha de pago</th><td>${pago.fechaPago}</td></tr>
    </table>

    <p><a href="${pageContext.request.contextPath}/pagos">Registrar otra salida</a></p>

</body>
</html>
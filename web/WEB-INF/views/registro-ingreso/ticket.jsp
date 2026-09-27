<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Ticket de ingreso</title>
</head>
<body>

    <h2>Ingreso registrado</h2>

    <table border="1" cellpadding="6">
        <tr><th>ID ticket</th><td>${ticket.id}</td></tr>
        <tr><th>ID vehículo</th><td>${ticket.idVehiculo}</td></tr>
        <tr><th>Hora entrada</th><td>${ticket.horaEntrada}</td></tr>
        <tr><th>Operador entrada</th><td>${ticket.idOperadorEntrada}</td></tr>
    </table>

    <p><a href="${pageContext.request.contextPath}/registros-ingreso">Registrar otro ingreso</a></p>

</body>
</html>
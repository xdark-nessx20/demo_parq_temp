<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Pago</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <h2>Pago registrado</h2>

    <table border="1" cellpadding="6">
        <tr><th>Vehículo</th><td>${vehiculoPlaca}</td></tr>
        <tr><th>Hora entrada</th><td>${horaEntrada}</td></tr>
        <tr><th>Hora salida</th><td>${horaSalida}</td></tr>
        <tr><th>Tarifa por hora</th><td>$${valorHoraTexto}</td></tr>
        <tr><th>Horas cobradas</th><td>${horas}</td></tr>
        <tr><th>Total a pagar</th><td>$${pago.valorTexto}</td></tr>
        <tr><th>Fecha de pago</th><td>${pago.fechaPagoTexto}</td></tr>
    </table>

    <p><a href="${pageContext.request.contextPath}/pagos">Registrar otra salida</a></p>

</body>
</html>
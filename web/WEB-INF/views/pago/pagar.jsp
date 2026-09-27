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

    <div class="page-head">
        <h2>Pago registrado</h2>
        <div class="page-head-acciones">
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/pagos">Volver</a>
        </div>
    </div>

    <table>
        <tr><th>Vehículo</th><td>${vehiculoPlaca}</td></tr>
        <tr><th>Hora entrada</th><td>${horaEntrada}</td></tr>
        <tr><th>Hora salida</th><td>${horaSalida}</td></tr>
        <tr><th>Tarifa por hora</th><td>$${valorHoraTexto}</td></tr>
        <tr><th>Horas cobradas</th><td>${horas}</td></tr>
        <tr><th>Total a pagar</th><td>$${pago.valorTexto}</td></tr>
        <tr><th>Fecha de pago</th><td>${pago.fechaPagoTexto}</td></tr>
    </table>

    <div class="acciones">
        <a class="boton" href="${pageContext.request.contextPath}/pagos?accion=registrar">Registrar otra salida</a>
    </div>

</body>
</html>
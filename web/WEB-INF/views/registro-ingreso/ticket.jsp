<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Ticket de ingreso</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <div class="page-head">
        <h2>Ingreso registrado</h2>
        <div class="page-head-acciones">
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/dentro">Volver</a>
        </div>
    </div>

    <table>
        <tr><th>Vehículo</th><td>${placas[ticket.idVehiculo]}</td></tr>
        <tr><th>Hora entrada</th><td>${ticket.horaEntradaTexto}</td></tr>
        <tr><th>Operador entrada</th><td>${operadores[ticket.idOperadorEntrada]}</td></tr>
    </table>

    <c:if test="${sinDueno}">
        <p class="mensaje mensaje-error">
            <strong>Vehículo sin dueño.</strong> No estaba registrado, así que entró sin propietario.
            Dile al cliente que se registre en la app y lo reclame (Mi cuenta → Reclamar vehículo).
        </p>
    </c:if>

    <div class="acciones">
        <a class="boton" href="${pageContext.request.contextPath}/registros-ingreso?accion=registrar">Registrar otro ingreso</a>
    </div>

</body>
</html>
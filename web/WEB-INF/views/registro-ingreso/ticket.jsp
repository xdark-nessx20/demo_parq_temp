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
        <span id="avisoSinDueno" hidden>Vehículo sin dueño: no estaba registrado. Dile al cliente que se registre en la app y lo reclame (Mi cuenta → Reclamar vehículo).</span>
        <jsp:include page="/WEB-INF/views/comunes/modal-mensaje.jsp" />
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                var t = document.getElementById('avisoSinDueno');
                if (t && window.mostrarMensaje) window.mostrarMensaje('warn', t.textContent.trim());
            });
        </script>
    </c:if>

    <div class="acciones">
        <a class="boton" href="${pageContext.request.contextPath}/registros-ingreso?accion=registrar">Registrar otro ingreso</a>
    </div>

</body>
</html>
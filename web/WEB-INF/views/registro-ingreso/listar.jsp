<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registros de ingreso</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <div class="page-head">
        <h2>Registros de ingreso</h2>
        <div class="page-head-acciones">
            <c:if test="${sessionScope.usuario.rolNombre == 'OPERADOR'}">
                <a class="boton" href="${pageContext.request.contextPath}/registros-ingreso?accion=registrar">Registrar ingreso</a>
                <a class="boton boton-secundario" href="${pageContext.request.contextPath}/pagos?accion=registrar">Registrar salida</a>
            </c:if>
        </div>
    </div>

    <c:choose>
        <c:when test="${empty ingresos}">
            <p>No hay registros de ingreso.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="6">
                <thead>
                    <tr>
                        <th>Vehículo</th>
                        <th>Hora entrada</th>
                        <th>Hora salida</th>
                        <th>Operador entrada</th>
                        <th>Operador salida</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="r" items="${ingresos}">
                        <tr>
                            <td><span class="placa">${placas[r.idVehiculo]}</span></td>
                            <td>${r.horaEntradaTexto}</td>
                            <td>${r.horaSalidaTexto}</td>
                            <td>${operadores[r.idOperadorEntrada]}</td>
                            <td>${operadores[r.idOperadorSalida]}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

    <jsp:include page="/WEB-INF/views/comunes/auto-refresco.jsp" />
</body>
</html>
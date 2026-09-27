<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Vehículos dentro</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <h1>Vehículos dentro del parqueadero</h1>

    <c:if test="${not empty sessionScope.mensaje}">
        <p class="mensaje mensaje-ok">${sessionScope.mensaje}</p>
        <c:remove var="mensaje" scope="session" />
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <p class="mensaje mensaje-error">${sessionScope.error}</p>
        <c:remove var="error" scope="session" />
    </c:if>

    <p><strong>${vehiculosDentro.size()}</strong> vehículo(s) adentro.</p>

    <c:choose>
        <c:when test="${empty vehiculosDentro}">
            <p>No hay vehículos dentro en este momento.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                    <tr>
                        <th>Placa</th>
                        <th>Tipo</th>
                        <th>Entrada</th>
                        <th>Tiempo</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="v" items="${vehiculosDentro}">
                        <tr>
                            <td><strong>${v.placa}</strong></td>
                            <td>${v.tipo}</td>
                            <td>${v.horaEntrada}</td>
                            <td>${v.tiempo}</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/dentro" method="post"
                                      onsubmit="return confirm('¿Registrar la salida de ${v.placa}?');">
                                    <input type="hidden" name="idRegistro" value="${v.id}" />
                                    <button type="submit" class="boton-verde boton-chico">Dar salida</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

    <div class="acciones">
        <a class="boton" href="${pageContext.request.contextPath}/registros-ingreso?accion=registrar">Registrar ingreso</a>
    </div>

</body>
</html>

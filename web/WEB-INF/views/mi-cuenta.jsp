<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mi cuenta</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <h1>Mi cuenta</h1>
    <p>Vehículos y movimientos de <strong>${sessionScope.usuario.nombre}</strong>.</p>

    <c:if test="${not empty sessionScope.mensaje}">
        <p class="mensaje mensaje-ok">${sessionScope.mensaje}</p>
        <c:remove var="mensaje" scope="session" />
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <p class="mensaje mensaje-error">${sessionScope.error}</p>
        <c:remove var="error" scope="session" />
    </c:if>

    <c:choose>
        <c:when test="${empty movimientos}">
            <p>No tiene vehículos ni movimientos registrados.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                    <tr>
                        <th>Placa</th>
                        <th>Tipo</th>
                        <th>Entrada</th>
                        <th>Salida</th>
                        <th>Valor</th>
                        <th>Estado</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="m" items="${movimientos}">
                        <tr>
                            <td><strong>${m.placa}</strong></td>
                            <td>${m.tipo}</td>
                            <td>${m.horaEntrada}</td>
                            <td>${m.horaSalida}</td>
                            <td>${m.valor}</td>
                            <td>${m.estado}</td>
                            <td>
                                <c:if test="${m.pagable}">
                                    <form action="${pageContext.request.contextPath}/mi-cuenta" method="post"
                                          onsubmit="return confirm('¿Pagar ${m.valor}?');">
                                        <input type="hidden" name="idPago" value="${m.idPago}" />
                                        <button type="submit" class="boton-verde boton-chico">Pagar</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Pagos</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <div class="page-head">
        <h2>Pagos registrados</h2>
        <div class="page-head-acciones">
            <c:if test="${sessionScope.usuario.rolNombre == 'OPERADOR'}">
                <a class="boton" href="${pageContext.request.contextPath}/pagos?accion=registrar">Registrar salida y pago</a>
            </c:if>
        </div>
    </div>

    <c:if test="${not empty sessionScope.mensaje}">
        <p class="mensaje mensaje-ok">${sessionScope.mensaje}</p>
        <c:remove var="mensaje" scope="session" />
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <p class="mensaje mensaje-error">${sessionScope.error}</p>
        <c:remove var="error" scope="session" />
    </c:if>

    <c:choose>
        <c:when test="${empty pagos}">
            <p>No hay pagos registrados.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                    <tr>
                        <th>Vehículo</th>
                        <th>Valor</th>
                        <th>Fecha de pago</th>
                        <th>Estado</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${pagos}">
                        <tr>
                            <td><span class="placa">${placasPorRegistro[p.idRegistroIngreso]}</span></td>
                            <td class="num">$${p.valorTexto}</td>
                            <td>${p.fechaPagoTexto}</td>
                            <td><span class="badge ${p.pagado ? 'badge-ok' : 'badge-warn'}">${p.estado}</span></td>
                            <td>
                                <c:if test="${not p.pagado and sessionScope.usuario.rolNombre == 'OPERADOR'}">
                                    <form action="${pageContext.request.contextPath}/pagos" method="post" data-pago>
                                        <input type="hidden" name="accion" value="cobrar" />
                                        <input type="hidden" name="idPago" value="${p.id}" />
                                        <button type="submit" class="boton-verde boton-chico">Cobrar</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

    <jsp:include page="/WEB-INF/views/comunes/modal-pago.jsp" />
    <jsp:include page="/WEB-INF/views/comunes/auto-refresco.jsp" />
</body>
</html>
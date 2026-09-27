<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Vehículos dentro</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />
    <jsp:include page="/WEB-INF/views/comunes/mensajes.jsp" />

    <h1>Vehículos dentro</h1>
    <p class="muted">Vehículos que están en el parqueadero ahora mismo.</p>

    <div class="stats">
        <div class="stat">
            <span class="stat-num">${vehiculosDentro.size()}</span>
            <span class="stat-label">Vehículos adentro</span>
        </div>
    </div>

    <div class="acciones">
        <a class="boton" href="${pageContext.request.contextPath}/registros-ingreso?accion=registrar">Registrar ingreso</a>
    </div>

    <c:choose>
        <c:when test="${empty vehiculosDentro}">
            <div class="card">
                <p class="muted">No hay vehículos dentro en este momento.</p>
            </div>
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
                            <td><span class="placa">${v.placa}</span></td>
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
</body>
</html>

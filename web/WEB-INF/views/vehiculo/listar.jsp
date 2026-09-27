<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Vehículos</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />
    <jsp:include page="/WEB-INF/views/comunes/mensajes.jsp" />

    <h2>Vehículos registrados</h2>

    <p><a href="${pageContext.request.contextPath}/vehiculos?accion=registrar">Registrar vehículo</a></p>

    <c:choose>
        <c:when test="${empty vehiculos}">
            <p>No hay vehículos registrados.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="6">
                <thead>
                    <tr>
                        <th>Placa</th>
                        <th>Cédula propietario</th>
                        <th>Tipo</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="v" items="${vehiculos}">
                        <tr>
                            <td><span class="placa">${v.placa}</span></td>
                            <td>${v.owner.cedula}</td>
                            <td>${v.tipo.nombre}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/vehiculos?accion=buscar&placa=${v.placa}">Ver</a>
                                <a href="${pageContext.request.contextPath}/vehiculos?accion=editar&placa=${v.placa}">Editar</a>
                                <form action="${pageContext.request.contextPath}/vehiculos" method="post"
                                      onsubmit="return confirm('¿Eliminar el vehículo ${v.placa}?');">
                                    <input type="hidden" name="accion" value="eliminar" />
                                    <input type="hidden" name="placa" value="${v.placa}" />
                                    <button type="submit" class="boton-chico">Eliminar</button>
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
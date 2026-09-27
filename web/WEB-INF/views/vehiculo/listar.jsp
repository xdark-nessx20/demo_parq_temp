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

    <div class="page-head">
        <h2>Vehículos registrados</h2>
        <div class="page-head-acciones">
            <a class="boton" href="${pageContext.request.contextPath}/vehiculos?accion=registrar">Registrar vehículo</a>
        </div>
    </div>

    <c:choose>
        <c:when test="${empty vehiculos}">
            <p>No hay vehículos registrados.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                    <tr>
                        <th>Placa</th>
                        <th>Cédula propietario</th>
                        <th>Dueño</th>
                        <th>Tipo</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="v" items="${vehiculos}">
                        <tr>
                            <td><span class="placa">${v.placa}</span></td>
                            <td>${empty v.owner ? '—' : v.owner.cedula}</td>
                            <td>${empty v.owner ? 'Sin dueño' : v.owner.nombre}</td>
                            <td>${v.tipo.nombre}</td>
                            <td>
                                <div class="acciones-tabla">
                                    <a class="boton boton-secundario boton-chico" href="${pageContext.request.contextPath}/vehiculos?accion=buscar&placa=${v.placa}">Ver</a>
                                    <a class="boton boton-secundario boton-chico" href="${pageContext.request.contextPath}/vehiculos?accion=editar&placa=${v.placa}">Editar</a>
                                    <form action="${pageContext.request.contextPath}/vehiculos" method="post"
                                          onsubmit="return confirm('¿Eliminar el vehículo ${v.placa}?');">
                                        <input type="hidden" name="accion" value="eliminar" />
                                        <input type="hidden" name="placa" value="${v.placa}" />
                                        <button type="submit" class="boton-rojo boton-chico">Eliminar</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

</body>
</html>
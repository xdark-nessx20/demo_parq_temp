<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Tipos de vehículo</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />
    <jsp:include page="/WEB-INF/views/comunes/mensajes.jsp" />

    <div class="page-head">
        <h2>Tipos de vehículo registrados</h2>
        <div class="page-head-acciones">
            <a class="boton" href="${pageContext.request.contextPath}/tipos-vehiculo?accion=registrar">Registrar tipo de vehículo</a>
        </div>
    </div>

    <c:choose>
        <c:when test="${empty tipos}">
            <p>No hay tipos de vehículo registrados.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="6">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="tipo" items="${tipos}">
                        <tr>
                            <td>${tipo.nombre}</td>
                            <td>
                                <div class="acciones-tabla">
                                    <a class="boton boton-secundario boton-chico" href="${pageContext.request.contextPath}/tipos-vehiculo?accion=buscar&nombre=${tipo.nombre}">Ver</a>
                                    <a class="boton boton-secundario boton-chico" href="${pageContext.request.contextPath}/tipos-vehiculo?accion=editar&id=${tipo.id}">Editar</a>
                                    <form action="${pageContext.request.contextPath}/tipos-vehiculo" method="post"
                                          onsubmit="return confirm('¿Eliminar el tipo ${tipo.nombre}?');">
                                        <input type="hidden" name="accion" value="eliminar" />
                                        <input type="hidden" name="id" value="${tipo.id}" />
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
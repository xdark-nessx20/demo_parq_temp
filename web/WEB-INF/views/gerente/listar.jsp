<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gerentes</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />
    <jsp:include page="/WEB-INF/views/comunes/mensajes.jsp" />

    <h2>Gerentes registrados</h2>

    <p><a href="${pageContext.request.contextPath}/gerentes?accion=registrar">Registrar gerente</a></p>

    <c:choose>
        <c:when test="${empty gerentes}">
            <p>No hay gerentes registrados.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="6">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Cédula</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="g" items="${gerentes}">
                        <tr>
                            <td>${g.nombre}</td>
                            <td>${g.cedula}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/gerentes?accion=buscar&cedula=${g.cedula}">Ver</a>
                                <a href="${pageContext.request.contextPath}/gerentes?accion=editar&cedula=${g.cedula}">Editar</a>
                                <form action="${pageContext.request.contextPath}/gerentes" method="post"
                                      onsubmit="return confirm('¿Eliminar a ${g.nombre}?');">
                                    <input type="hidden" name="accion" value="eliminar" />
                                    <input type="hidden" name="cedula" value="${g.cedula}" />
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

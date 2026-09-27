<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Tipos de vehículo</title>
</head>
<body>

    <h2>Tipos de vehículo registrados</h2>

    <p>
        <a href="${pageContext.request.contextPath}/WEB-INF/views/tipo-vehiculo/registrar.jsp">
        <!-- si tienes un Servlet/acción para mostrar el form, apunta ahí en vez de a WEB-INF directo -->
        </a>
    </p>

    <c:choose>
        <c:when test="${empty tipos}">
            <p>No hay tipos de vehículo registrados.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="6">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Descripción</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="tipo" items="${tipos}">
                        <tr>
                            <td>${tipo.nombre}</td>
                            <td>${tipo.descripcion}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/tipos-vehiculo?accion=buscar&nombre=${tipo.nombre}">
                                    Ver detalle
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

</body>
</html>
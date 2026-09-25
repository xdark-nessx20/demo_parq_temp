<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Clientes</title>
</head>
<body>

    <h2>Clientes registrados</h2>

    <c:choose>
        <c:when test="${empty clientes}">
            <p>No hay clientes registrados.</p>
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
                    <c:forEach var="c" items="${clientes}">
                        <tr>
                            <td>${c.nombre}</td>
                            <td>${c.cedula}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/clientes?accion=buscar&cedula=${c.cedula}">
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
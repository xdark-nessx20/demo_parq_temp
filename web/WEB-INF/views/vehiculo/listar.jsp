<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Vehículos</title>
</head>
<body>

    <h2>Vehículos registrados</h2>

    <c:choose>
        <c:when test="${empty vehiculos}">
            <p>No hay vehículos registrados.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="6">
                <thead>
                    <tr>
                        <th>Placa</th>
                        <th>Marca</th>
                        <th>Cédula propietario</th>
                        <th>Tipo</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="v" items="${vehiculos}">
                        <tr>
                            <td>${v.placa}</td>
                            <td>${v.marca}</td>
                            <td>${v.ownerCedula}</td>
                            <td>${v.tipoVehiculo}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/vehiculos?accion=buscar&placa=${v.placa}">
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
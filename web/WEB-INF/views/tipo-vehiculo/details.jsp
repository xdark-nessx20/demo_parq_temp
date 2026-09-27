<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Detalle del tipo de vehículo</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <h2>Detalle: ${tipo.nombre}</h2>

    <table border="1" cellpadding="6">
        <tr>
            <th>Nombre</th>
            <td>${tipo.nombre}</td>
        </tr>
    </table>

    <p>
        <a href="${pageContext.request.contextPath}/tipos-vehiculo">Volver al listado</a>
    </p>

</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Detalle del tipo de vehículo</title>
</head>
<body>

    <h2>Detalle: ${tipo.nombre}</h2>

    <table border="1" cellpadding="6">
        <tr>
            <th>Nombre</th>
            <td>${tipo.nombre}</td>
        </tr>
        <tr>
            <th>Descripción</th>
            <td>${tipo.descripcion}</td>
        </tr>
    </table>

    <p>
        <a href="${pageContext.request.contextPath}/tipos-vehiculo">Volver al listado</a>
    </p>

</body>
</html>
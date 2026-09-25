<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Detalle del vehículo</title>
</head>
<body>

    <h2>Detalle: ${vehiculo.placa}</h2>

    <table border="1" cellpadding="6">
        <tr>
            <th>Placa</th>
            <td>${vehiculo.placa}</td>
        </tr>
        <tr>
            <th>Marca</th>
            <td>${vehiculo.marca}</td>
        </tr>
        <tr>
            <th>Cédula propietario</th>
            <td>${vehiculo.ownerCedula}</td>
        </tr>
        <tr>
            <th>Tipo</th>
            <td>${vehiculo.tipoVehiculo}</td>
        </tr>
    </table>

    <p>
        <a href="${pageContext.request.contextPath}/vehiculos">Volver al listado</a>
    </p>

</body>
</html>
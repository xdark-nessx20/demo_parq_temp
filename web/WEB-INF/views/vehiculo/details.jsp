<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Detalle del vehículo</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <div class="page-head">
        <h2>Detalle: ${vehiculo.placa}</h2>
        <div class="page-head-acciones">
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/vehiculos">← Volver</a>
        </div>
    </div>

    <table border="1" cellpadding="6">
        <tr>
            <th>Placa</th>
            <td>${vehiculo.placa}</td>
        </tr>
        <tr>
            <th>Cédula propietario</th>
            <td>${vehiculo.owner.cedula}</td>
        </tr>
        <tr>
            <th>Tipo</th>
            <td>${vehiculo.tipo.nombre}</td>
        </tr>
    </table>

</body>
</html>
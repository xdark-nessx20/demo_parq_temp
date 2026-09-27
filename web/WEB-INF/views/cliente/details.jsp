<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Detalle del cliente</title>
</head>
<body>

    <h2>Detalle: ${cliente.nombre}</h2>

    <table border="1" cellpadding="6">
        <tr>
            <th>Nombre</th>
            <td>${cliente.nombre}</td>
        </tr>
        <tr>
            <th>Cédula</th>
            <td>${cliente.cedula}</td>
        </tr>
    </table>

    <p>
        <a href="${pageContext.request.contextPath}/clientes">Volver al listado</a>
    </p>

</body>
</html>
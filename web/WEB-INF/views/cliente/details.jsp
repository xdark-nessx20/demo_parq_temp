<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Detalle del cliente</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <div class="page-head">
        <h2>Detalle: ${cliente.nombre}</h2>
        <div class="page-head-acciones">
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/clientes">Volver</a>
        </div>
    </div>

    <table>
        <tr>
            <th>Nombre</th>
            <td>${cliente.nombre}</td>
        </tr>
        <tr>
            <th>Cédula</th>
            <td>${cliente.cedula}</td>
        </tr>
    </table>

</body>
</html>
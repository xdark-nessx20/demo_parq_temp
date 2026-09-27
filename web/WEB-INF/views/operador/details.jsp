<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Detalle del operador</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <div class="page-head">
        <h2>Detalle: ${operador.nombre}</h2>
        <div class="page-head-acciones">
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/operadores">Volver</a>
        </div>
    </div>

    <table>
        <tr>
            <th>Nombre</th>
            <td>${operador.nombre}</td>
        </tr>
        <tr>
            <th>Cédula</th>
            <td>${operador.cedula}</td>
        </tr>
    </table>

</body>
</html>

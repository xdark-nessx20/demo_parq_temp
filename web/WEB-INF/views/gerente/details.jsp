<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Detalle del gerente</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <div class="page-head">
        <h2>Detalle: ${gerente.nombre}</h2>
        <div class="page-head-acciones">
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/gerentes">← Volver</a>
        </div>
    </div>

    <table border="1" cellpadding="6">
        <tr>
            <th>Nombre</th>
            <td>${gerente.nombre}</td>
        </tr>
        <tr>
            <th>Cédula</th>
            <td>${gerente.cedula}</td>
        </tr>
    </table>

</body>
</html>

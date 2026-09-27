<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar operador</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <div class="page-head">
        <h2>Registrar operador</h2>
        <div class="page-head-acciones">
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/operadores">Volver</a>
        </div>
    </div>

<jsp:include page="/WEB-INF/views/comunes/mensajes.jsp" />

    <form action="${pageContext.request.contextPath}/operadores" method="post">

        <div>
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre"
                   value="${param.nombre}" required />
        </div>

        <div>
            <label for="cedula">Cédula:</label>
            <input type="text" id="cedula" name="cedula"
                   value="${param.cedula}" required />
        </div>

        <div>
            <label for="contrasena">Contraseña:</label>
            <input type="password" id="contrasena" name="contrasena" value="${param.contrasena}" data-pw required />
        </div>
        <jsp:include page="/WEB-INF/views/comunes/reglas-password.jsp" />

        <button type="submit">Registrar</button>
    </form>

</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar cliente</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <h2>Registrar cliente</h2>

    <c:if test="${not empty error}">
        <p style="color:red;"><strong>${error}</strong></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/clientes" method="post">

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
            <label for="contrasena">Contraseña (para entrar a "Mi cuenta"):</label>
            <input type="password" id="contrasena" name="contrasena" value="${param.contrasena}" data-pw required />
        </div>
        <jsp:include page="/WEB-INF/views/comunes/reglas-password.jsp" />

        <button type="submit">Registrar</button>
    </form>

</body>
</html>
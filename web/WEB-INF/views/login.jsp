<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Iniciar sesión</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <h2>Iniciar sesión</h2>

    <c:if test="${not empty error}">
        <p style="color:red;"><strong>${error}</strong></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">

        <div>
            <label for="cedula">Cédula:</label>
            <input type="text" id="cedula" name="cedula" value="${param.cedula}" required />
        </div>

        <div>
            <label for="contrasena">Contraseña:</label>
            <input type="password" id="contrasena" name="contrasena" required />
        </div>

        <button type="submit">Entrar</button>
    </form>

    <p><a href="${pageContext.request.contextPath}/index.jsp">Volver al inicio</a></p>

</body>
</html>

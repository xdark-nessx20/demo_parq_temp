<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Iniciar sesión</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <div class="login-wrap">
        <h1>Iniciar sesión</h1>
        <p class="muted">Ingrese su cédula y contraseña.</p>

        <c:if test="${not empty error}">
            <p class="mensaje mensaje-error">${error}</p>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div>
                <label for="cedula">Cédula</label>
                <input type="text" id="cedula" name="cedula" value="${param.cedula}" required autofocus />
            </div>
            <div>
                <label for="contrasena">Contraseña</label>
                <input type="password" id="contrasena" name="contrasena" required />
            </div>
            <button type="submit">Entrar</button>
        </form>
    </div>
</body>
</html>

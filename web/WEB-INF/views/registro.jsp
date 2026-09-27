<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Crear cuenta</title>
</head>
<body>
    <div class="auth">
        <aside class="auth-side">
            <span class="auth-brand">Parqueamesta</span>
            <p class="auth-tag">Crea tu cuenta de cliente</p>
            <ul>
                <li>Registra tus vehículos</li>
                <li>Consulta tus tickets y pagos</li>
                <li>Paga en línea</li>
            </ul>
        </aside>

        <main class="auth-main">
            <h1>Crear cuenta</h1>
            <p class="muted">Para clientes: registra tus vehículos y paga tus parqueos.</p>

            <c:if test="${not empty error}">
                <p class="mensaje mensaje-error">${error}</p>
            </c:if>

            <form action="${pageContext.request.contextPath}/registro" method="post">
                <div>
                    <label for="nombre">Nombre completo</label>
                    <input type="text" id="nombre" name="nombre" value="${param.nombre}" required />
                </div>
                <div>
                    <label for="cedula">Cédula</label>
                    <input type="text" id="cedula" name="cedula" value="${param.cedula}" required />
                </div>
                <div>
                    <label for="contrasena">Contraseña</label>
                    <input type="password" id="contrasena" name="contrasena" required minlength="6" />
                </div>
                <button type="submit">Crear cuenta</button>
            </form>

            <p class="muted">¿Ya tienes cuenta?
                <a href="${pageContext.request.contextPath}/login">Inicia sesión</a>
            </p>
        </main>
    </div>
</body>
</html>

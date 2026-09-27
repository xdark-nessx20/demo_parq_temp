<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Crear cuenta</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <div class="auth">
        <aside class="auth-side">
            <span class="auth-brand">Parqueamesta</span>
            <p class="auth-tag">Crea tu cuenta de cliente</p>

            <div class="role-cards">
                <div class="role-card-lg">
                    <div class="rc-head">
                        <span class="rc-icon">C</span>
                        <div><strong>Cliente</strong><span class="rc-sub">Esta cuenta es para clientes</span></div>
                    </div>
                    <ul>
                        <li>Registra tus vehículos</li>
                        <li>Consulta tus tickets y pagos</li>
                        <li>Paga en línea</li>
                    </ul>
                </div>
            </div>
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
                    <input type="text" id="nombre" name="nombre" value="${param.nombre}" autocomplete="name" required />
                </div>
                <div>
                    <label for="cedula">Cédula</label>
                    <input type="text" id="cedula" name="cedula" value="${param.cedula}"
                           autocomplete="username" spellcheck="false" required />
                </div>
                <div>
                    <label for="contrasena">Contraseña</label>
                    <input type="password" id="contrasena" name="contrasena" data-pw autocomplete="new-password" required />
                </div>
                <jsp:include page="/WEB-INF/views/comunes/reglas-password.jsp" />
                <button type="submit">Crear cuenta</button>
            </form>

            <p class="muted">¿Ya tienes cuenta?
                <a href="${pageContext.request.contextPath}/login">Inicia sesión</a>
            </p>
        </main>
    </div>
</body>
</html>

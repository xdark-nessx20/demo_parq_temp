<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Iniciar sesión</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <div class="auth">
        <aside class="auth-side">
            <span class="auth-brand">Parqueamesta</span>
            <p class="auth-tag">Sistema de gestión de parqueadero</p>

            <div class="role-cards">
                <div class="role-card-lg">
                    <div class="rc-head">
                        <span class="rc-icon">O</span>
                        <div><strong>Operador</strong><span class="rc-sub">Superpoderes para el portero</span></div>
                    </div>
                    <ul>
                        <li>Registrar entradas y salidas</li>
                        <li>Cobrar pagos</li>
                        <li>Ver vehículos dentro</li>
                    </ul>
                </div>
                <div class="role-card-lg">
                    <div class="rc-head">
                        <span class="rc-icon">G</span>
                        <div><strong>Gerente</strong><span class="rc-sub">Superpoderes para ti</span></div>
                    </div>
                    <ul>
                        <li>Administrar tarifas y tipos</li>
                        <li>Gestionar clientes y vehículos</li>
                        <li>Ver los pagos</li>
                    </ul>
                </div>
                <div class="role-card-lg">
                    <div class="rc-head">
                        <span class="rc-icon">C</span>
                        <div><strong>Cliente</strong><span class="rc-sub">Para tus clientes</span></div>
                    </div>
                    <ul>
                        <li>Registrar sus vehículos</li>
                        <li>Ver sus tickets</li>
                        <li>Pagar en línea</li>
                    </ul>
                </div>
            </div>
        </aside>

        <main class="auth-main">
            <h1>Entra a tu cuenta</h1>
            <p class="muted">Ingresa tu cédula y contraseña.</p>

            <c:if test="${not empty error}">
                <p class="mensaje mensaje-error">${error}</p>
            </c:if>
            <c:if test="${not empty sessionScope.mensaje}">
                <p class="mensaje mensaje-ok">${sessionScope.mensaje}</p>
                <c:remove var="mensaje" scope="session" />
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
                <button type="submit">Iniciar sesión</button>
            </form>

            <p class="muted">¿No tienes cuenta?
                <a href="${pageContext.request.contextPath}/registro">Regístrate</a>
            </p>

            <h3>Roles</h3>
            <div class="roles">
                <div class="role-card"><strong>Operador</strong><span>Registra ingresos, da salida y cobra vehículos.</span></div>
                <div class="role-card"><strong>Gerente</strong><span>Administra tarifas, tipos de vehículo y personas.</span></div>
                <div class="role-card"><strong>Cliente</strong><span>Registra sus vehículos y paga en línea.</span></div>
            </div>
        </main>
    </div>
</body>
</html>

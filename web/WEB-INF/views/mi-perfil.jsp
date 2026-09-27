<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Mi perfil</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />
    <jsp:include page="/WEB-INF/views/comunes/mensajes.jsp" />

    <h1>Mi perfil</h1>
    <p class="muted">Cédula: <strong>${sessionScope.usuario.cedula}</strong> ·
        Rol: <span class="badge badge-info">${sessionScope.usuario.rolNombre}</span></p>

    <h2>Datos</h2>
    <form action="${pageContext.request.contextPath}/mi-perfil" method="post">
        <input type="hidden" name="accion" value="nombre" />
        <div>
            <label for="nombre">Nombre</label>
            <input type="text" id="nombre" name="nombre" value="${sessionScope.usuario.nombre}" required />
        </div>
        <button type="submit">Guardar</button>
    </form>

    <h2>Cambiar contraseña</h2>
    <form id="formContrasena" action="${pageContext.request.contextPath}/mi-perfil" method="post">
        <input type="hidden" name="accion" value="contrasena" />
        <div>
            <label for="actual">Contraseña actual</label>
            <input type="password" id="actual" name="actual" required />
        </div>
        <div>
            <label for="nueva">Nueva contraseña</label>
            <input type="password" id="nueva" name="nueva" data-pw required />
        </div>
        <jsp:include page="/WEB-INF/views/comunes/reglas-password.jsp" />
        <button type="submit">Cambiar contraseña</button>
    </form>

    <jsp:include page="/WEB-INF/views/comunes/modal-mensaje.jsp" />
    <script>
        // Envia el cambio de contrasena por AJAX: muestra un modal y conserva los
        // campos si hay error/advertencia; los limpia si fue exitoso.
        (function () {
            var form = document.getElementById('formContrasena');
            if (!form) return;

            var MENSAJES = {
                OK:                 ['ok',    'Contraseña actualizada correctamente.'],
                ACTUAL_INCORRECTA:  ['error', 'La contraseña actual no coincide con la del sistema.'],
                IGUAL_A_ANTERIOR:   ['warn',  'La nueva contraseña es igual a la anterior.'],
                NO_CUMPLE:          ['error', 'La nueva contraseña no cumple con los requisitos.']
            };

            form.addEventListener('submit', function (e) {
                e.preventDefault();
                fetch(form.action, {
                    method: 'POST',
                    body: new FormData(form),
                    headers: { 'X-Requested-With': 'fetch' }
                })
                .then(function (r) { return r.text(); })
                .then(function (res) {
                    var codigo = res.trim();
                    var m = MENSAJES[codigo] || ['error', 'No se pudo cambiar la contraseña.'];
                    window.mostrarMensaje(m[0], m[1]);
                    if (codigo === 'OK') form.reset();
                })
                .catch(function () {});
            });
        })();
    </script>
</body>
</html>

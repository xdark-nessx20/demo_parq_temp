<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Parqueadero</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <c:choose>
        <c:when test="${not empty sessionScope.usuario}">
            <h1>Hola, ${sessionScope.usuario.nombre}</h1>
            <p>Rol: <strong>${sessionScope.usuario.rolNombre}</strong></p>

            <div class="acciones">
                <a class="boton" href="${pageContext.request.contextPath}/registros-ingreso?accion=registrar">Registrar ingreso</a>
                <a class="boton" href="${pageContext.request.contextPath}/pagos?accion=registrar">Registrar salida</a>
                <a class="boton" href="${pageContext.request.contextPath}/registros-ingreso">Ver registros</a>
            </div>
        </c:when>
        <c:otherwise>
            <h1>Bienvenido al parqueadero</h1>
            <p>Inicie sesión para continuar.</p>
            <p><a class="boton" href="${pageContext.request.contextPath}/login">Iniciar sesión</a></p>
        </c:otherwise>
    </c:choose>

</body>
</html>

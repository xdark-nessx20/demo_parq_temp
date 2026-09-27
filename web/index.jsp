<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Parqueadero</title>
</head>
<body>

    <h1>Parqueadero</h1>

    <c:choose>
        <c:when test="${not empty sessionScope.usuario}">
            <p>Bienvenido, <strong>${sessionScope.usuario.nombre}</strong> (${sessionScope.usuario.rol})</p>

            <ul>
                <li><a href="${pageContext.request.contextPath}/registros-ingreso">Registros de ingreso</a></li>
                <li><a href="${pageContext.request.contextPath}/pagos">Pagos</a></li>
                <li><a href="${pageContext.request.contextPath}/tarifas">Tarifas</a></li>
                <li><a href="${pageContext.request.contextPath}/vehiculos">Vehículos</a></li>
                <li><a href="${pageContext.request.contextPath}/clientes">Clientes</a></li>
                <li><a href="${pageContext.request.contextPath}/operadores">Operadores</a></li>
                <li><a href="${pageContext.request.contextPath}/gerentes">Gerentes</a></li>
            </ul>

            <p><a href="${pageContext.request.contextPath}/logout">Cerrar sesión</a></p>
        </c:when>
        <c:otherwise>
            <p>Bienvenido. Por favor inicie sesión.</p>
            <p><a href="${pageContext.request.contextPath}/login">Iniciar sesión</a></p>
        </c:otherwise>
    </c:choose>

</body>
</html>

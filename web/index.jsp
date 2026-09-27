<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Parqueamesta</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <c:choose>
        <c:when test="${not empty sessionScope.usuario}">
            <h1>Hola, ${sessionScope.usuario.nombre}</h1>
            <p class="muted">Sesión iniciada como ${sessionScope.usuario.rolNombre}.</p>

            <div class="acciones">
                <c:if test="${sessionScope.usuario.rolNombre == 'OPERADOR' or sessionScope.usuario.rolNombre == 'GERENTE'}">
                    <a class="boton" href="${pageContext.request.contextPath}/registros-ingreso?accion=registrar">Registrar ingreso</a>
                    <a class="boton boton-gris" href="${pageContext.request.contextPath}/dentro">Vehículos dentro</a>
                    <a class="boton boton-gris" href="${pageContext.request.contextPath}/pagos">Pagos</a>
                </c:if>
                <c:if test="${sessionScope.usuario.rolNombre == 'CLIENTE'}">
                    <a class="boton" href="${pageContext.request.contextPath}/mi-cuenta">Mi cuenta</a>
                </c:if>
            </div>
        </c:when>
        <c:otherwise>
            <div class="login-wrap">
                <div class="card" style="text-align:center">
                    <h1>Parqueamesta</h1>
                    <p class="muted">Sistema de gestión de parqueadero.</p>
                    <p style="margin-top:18px"><a class="boton" href="${pageContext.request.contextPath}/login">Iniciar sesión</a></p>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</body>
</html>

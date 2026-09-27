<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
<nav class="navbar">
    <a class="brand" href="${pageContext.request.contextPath}/index.jsp">Parqueadero</a>

    <c:if test="${not empty sessionScope.usuario}">
        <c:if test="${sessionScope.usuario.rolNombre == 'OPERADOR' or sessionScope.usuario.rolNombre == 'GERENTE'}">
            <a href="${pageContext.request.contextPath}/dentro">Vehículos dentro</a>
            <a href="${pageContext.request.contextPath}/registros-ingreso">Ingreso</a>
            <a href="${pageContext.request.contextPath}/pagos">Salida / Pagos</a>
        </c:if>
        <c:if test="${sessionScope.usuario.rolNombre == 'GERENTE'}">
            <a href="${pageContext.request.contextPath}/vehiculos">Vehículos</a>
            <a href="${pageContext.request.contextPath}/tarifas">Tarifas</a>
            <a href="${pageContext.request.contextPath}/tipos-vehiculo">Tipos</a>
            <a href="${pageContext.request.contextPath}/clientes">Clientes</a>
            <a href="${pageContext.request.contextPath}/operadores">Operadores</a>
            <a href="${pageContext.request.contextPath}/gerentes">Gerentes</a>
        </c:if>
        <c:if test="${sessionScope.usuario.rolNombre == 'CLIENTE'}">
            <a href="${pageContext.request.contextPath}/mi-cuenta">Mi cuenta</a>
        </c:if>
        <span class="usuario">
            ${sessionScope.usuario.nombre} (${sessionScope.usuario.rolNombre})
            <a href="${pageContext.request.contextPath}/logout">Salir</a>
        </span>
    </c:if>

    <c:if test="${empty sessionScope.usuario}">
        <span class="usuario">
            <a href="${pageContext.request.contextPath}/login">Iniciar sesión</a>
        </span>
    </c:if>
</nav>

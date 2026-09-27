<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
<nav class="navbar">
    <a class="brand" href="${pageContext.request.contextPath}/index.jsp">Parqueamesta</a>

    <c:if test="${not empty sessionScope.usuario}">
        <c:if test="${sessionScope.usuario.rolNombre == 'OPERADOR' or sessionScope.usuario.rolNombre == 'GERENTE'}">
            <a class="nav-link" href="${pageContext.request.contextPath}/dentro">Vehículos dentro</a>
        </c:if>
        <c:if test="${sessionScope.usuario.rolNombre == 'OPERADOR' or sessionScope.usuario.rolNombre == 'GERENTE'}">
            <a class="nav-link" href="${pageContext.request.contextPath}/registros-ingreso">Ingreso</a>
        </c:if>
        <c:if test="${sessionScope.usuario.rolNombre == 'OPERADOR' or sessionScope.usuario.rolNombre == 'GERENTE'}">
            <a class="nav-link" href="${pageContext.request.contextPath}/pagos">Pagos</a>
        </c:if>
        <c:if test="${sessionScope.usuario.rolNombre == 'GERENTE'}">
            <a class="nav-link" href="${pageContext.request.contextPath}/vehiculos">Vehículos</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/tarifas">Tarifas</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/tipos-vehiculo">Tipos</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/clientes">Clientes</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/operadores">Operadores</a>
        </c:if>
        <c:if test="${sessionScope.usuario.rolNombre == 'CLIENTE'}">
            <a class="nav-link" href="${pageContext.request.contextPath}/mi-cuenta">Mi cuenta</a>
        </c:if>

        <span class="usuario">
            <span class="quien">${sessionScope.usuario.nombre}</span>
            <span class="rol">${sessionScope.usuario.rolNombre}</span>
            <a class="nav-link" href="${pageContext.request.contextPath}/mi-perfil">Mi perfil</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/logout">Salir</a>
        </span>
    </c:if>

    <c:if test="${empty sessionScope.usuario}">
        <span class="usuario">
            <a class="nav-link" href="${pageContext.request.contextPath}/login">Iniciar sesión</a>
        </span>
    </c:if>
</nav>
<jsp:include page="/WEB-INF/views/comunes/ver-contrasena.jsp" />

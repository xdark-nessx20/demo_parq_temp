<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Clientes</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />
    <jsp:include page="/WEB-INF/views/comunes/mensajes.jsp" />

    <div class="page-head">
        <h2>Clientes registrados</h2>
        <div class="page-head-acciones">
            <a class="boton" href="${pageContext.request.contextPath}/clientes?accion=registrar">Registrar cliente</a>
        </div>
    </div>

    <c:choose>
        <c:when test="${empty clientes}">
            <p>No hay clientes registrados.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="6">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Cédula</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="c" items="${clientes}">
                        <tr>
                            <td>${c.nombre}</td>
                            <td>${c.cedula}</td>
                            <td>
                                <div class="acciones-tabla">
                                    <a class="boton boton-secundario boton-chico" href="${pageContext.request.contextPath}/clientes?accion=buscar&cedula=${c.cedula}">Ver</a>
                                    <a class="boton boton-secundario boton-chico" href="${pageContext.request.contextPath}/clientes?accion=editar&cedula=${c.cedula}">Editar</a>
                                    <form action="${pageContext.request.contextPath}/clientes" method="post"
                                          onsubmit="return confirm('¿Eliminar a ${c.nombre}?');">
                                        <input type="hidden" name="accion" value="eliminar" />
                                        <input type="hidden" name="cedula" value="${c.cedula}" />
                                        <button type="submit" class="boton-rojo boton-chico">Eliminar</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

</body>
</html>
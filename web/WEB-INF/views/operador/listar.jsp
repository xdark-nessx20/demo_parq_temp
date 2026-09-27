<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Operadores</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />
    <jsp:include page="/WEB-INF/views/comunes/mensajes.jsp" />

    <div class="page-head">
        <h2>Operadores registrados</h2>
        <div class="page-head-acciones">
            <a class="boton" href="${pageContext.request.contextPath}/operadores?accion=registrar">Registrar operador</a>
        </div>
    </div>

    <c:choose>
        <c:when test="${empty operadores}">
            <p>No hay operadores registrados.</p>
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
                    <c:forEach var="o" items="${operadores}">
                        <tr>
                            <td>${o.nombre}</td>
                            <td>${o.cedula}</td>
                            <td>
                                <div class="acciones-tabla">
                                    <a class="boton boton-secundario boton-chico" href="${pageContext.request.contextPath}/operadores?accion=buscar&cedula=${o.cedula}">Ver</a>
                                    <a class="boton boton-secundario boton-chico" href="${pageContext.request.contextPath}/operadores?accion=editar&cedula=${o.cedula}">Editar</a>
                                    <form action="${pageContext.request.contextPath}/operadores" method="post"
                                          onsubmit="return confirm('¿Eliminar a ${o.nombre}?');">
                                        <input type="hidden" name="accion" value="eliminar" />
                                        <input type="hidden" name="cedula" value="${o.cedula}" />
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

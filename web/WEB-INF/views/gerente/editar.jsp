<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar gerente</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <div class="page-head">
        <h2>Editar gerente</h2>
        <div class="page-head-acciones">
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/gerentes">Volver</a>
        </div>
    </div>

    <c:if test="${not empty error}">
        <p class="mensaje mensaje-error">${error}</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/gerentes" method="post">
        <input type="hidden" name="accion" value="editar" />
        <input type="hidden" name="cedula" value="${gerente.cedula}" />
        <div>
            <label>Cédula (no modificable):</label>
            <input type="text" value="${gerente.cedula}" disabled />
        </div>
        <div>
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" value="${gerente.nombre}" required />
        </div>
        <button type="submit">Guardar cambios</button>
    </form>
</body>
</html>

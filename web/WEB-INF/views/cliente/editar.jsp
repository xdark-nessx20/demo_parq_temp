<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar cliente</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <div class="page-head">
        <h2>Editar cliente</h2>
        <div class="page-head-acciones">
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/clientes">Volver</a>
        </div>
    </div>

<jsp:include page="/WEB-INF/views/comunes/mensajes.jsp" />

    <form action="${pageContext.request.contextPath}/clientes" method="post">
        <input type="hidden" name="accion" value="editar" />
        <input type="hidden" name="cedula" value="${cliente.cedula}" />
        <div>
            <label>Cédula (no modificable):</label>
            <input type="text" value="${cliente.cedula}" disabled />
        </div>
        <div>
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" value="${cliente.nombre}" required />
        </div>
        <button type="submit">Guardar cambios</button>
    </form>
</body>
</html>

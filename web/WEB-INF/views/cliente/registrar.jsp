<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar cliente</title>
</head>
<body>

    <h2>Registrar cliente</h2>

    <c:if test="${not empty error}">
        <p style="color:red;"><strong>${error}</strong></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/clientes" method="post">

        <div>
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre"
                   value="${param.nombre}" required />
        </div>

        <div>
            <label for="cedula">Cédula:</label>
            <input type="text" id="cedula" name="cedula"
                   value="${param.cedula}" required />
        </div>

        <button type="submit">Registrar</button>
    </form>

</body>
</html>
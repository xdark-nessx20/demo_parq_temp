<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar ingreso</title>
</head>
<body>

    <h2>Registrar ingreso de vehículo</h2>

    <c:if test="${not empty error}">
        <p style="color:red;"><strong>${error}</strong></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/registros-ingreso" method="post">

        <div>
            <label for="idVehiculo">ID del vehículo:</label>
            <input type="text" id="idVehiculo" name="idVehiculo"
                   value="${param.idVehiculo}" required />
        </div>

        <div>
            <label for="idOperador">ID del operador:</label>
            <input type="text" id="idOperador" name="idOperador"
                   value="${param.idOperador}" required />
        </div>

        <button type="submit">Registrar entrada</button>
    </form>

</body>
</html>
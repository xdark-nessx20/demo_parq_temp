<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar salida</title>
</head>
<body>

    <h2>Registrar salida de vehículo</h2>

    <c:if test="${not empty error}">
        <p style="color:red;"><strong>${error}</strong></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/pagos" method="post">

        <div>
            <label for="idRegistro">ID del ticket:</label>
            <input type="text" id="idRegistro" name="idRegistro"
                   value="${param.idRegistro}" required />
        </div>

        <div>
            <label for="idTipoVehiculo">ID del tipo de vehículo:</label>
            <input type="text" id="idTipoVehiculo" name="idTipoVehiculo"
                   value="${param.idTipoVehiculo}" required />
        </div>

        <div>
            <label for="idOperador">ID del operador:</label>
            <input type="text" id="idOperador" name="idOperador"
                   value="${param.idOperador}" required />
        </div>

        <button type="submit">Registrar salida</button>
    </form>

</body>
</html>
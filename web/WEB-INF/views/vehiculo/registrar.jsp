<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar vehículo</title>
</head>
<body>

    <h2>Registrar vehículo</h2>

    <c:if test="${not empty error}">
        <p style="color:red;"><strong>${error}</strong></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/vehiculos" method="post">

        <div>
            <label for="placa">Placa:</label>
            <input type="text" id="placa" name="placa"
                   value="${param.placa}" required />
        </div>

        <div>
            <label for="marca">Marca:</label>
            <input type="text" id="marca" name="marca"
                   value="${param.marca}" required />
        </div>

        <div>
            <label for="cedulaCliente">Cédula del propietario:</label>
            <input type="text" id="cedulaCliente" name="cedulaCliente"
                   value="${param.cedulaCliente}" required />
        </div>

        <div>
            <label for="tipoVehiculo">Tipo de vehículo:</label>
            <input type="text" id="tipoVehiculo" name="tipoVehiculo"
                   value="${param.tipoVehiculo}" required />
        </div>

        <button type="submit">Registrar</button>
    </form>

</body>
</html>
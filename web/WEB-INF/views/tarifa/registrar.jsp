<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar tarifa</title>
</head>
<body>

    <h2>Registrar nueva tarifa</h2>

    <c:if test="${not empty error}">
        <p style="color:red;"><strong>${error}</strong></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/tarifas" method="post">
        <input type="hidden" name="accion" value="registrar" />

        <div>
            <label for="idTipoVehiculo">ID del tipo de vehículo:</label>
            <input type="text" id="idTipoVehiculo" name="idTipoVehiculo"
                   value="${param.idTipoVehiculo}" required />
        </div>

        <div>
            <label for="valorHora">Valor por hora:</label>
            <input type="text" id="valorHora" name="valorHora"
                   value="${param.valorHora}" required />
        </div>

        <div>
            <label for="anioVigencia">Año de vigencia:</label>
            <input type="number" id="anioVigencia" name="anioVigencia"
                   value="${param.anioVigencia}" required />
        </div>

        <button type="submit">Registrar</button>
    </form>

    <p><a href="${pageContext.request.contextPath}/tarifas">Volver a tarifas</a></p>

</body>
</html>
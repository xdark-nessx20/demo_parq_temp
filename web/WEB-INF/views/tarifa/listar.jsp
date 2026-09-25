<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Tarifas</title>
</head>
<body>

    <h2>Tarifas vigentes</h2>

    <c:if test="${not empty error}">
        <p style="color:red;"><strong>${error}</strong></p>
    </c:if>

    <c:choose>
        <c:when test="${empty tarifas}">
            <p>No hay tarifas registradas.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="6">
                <thead>
                    <tr>
                        <th>ID tarifa</th>
                        <th>ID tipo vehículo</th>
                        <th>Valor por hora</th>
                        <th>Año vigencia</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="t" items="${tarifas}">
                        <tr>
                            <td>${t.id}</td>
                            <td>${t.idTipoVehiculo}</td>
                            <td>$${t.valorHora}</td>
                            <td>${t.anioVigencia}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <h3>Actualizar precio</h3>
            <form action="${pageContext.request.contextPath}/tarifas" method="post">
                <div>
                    <label for="idTarifa">ID tarifa:</label>
                    <input type="text" id="idTarifa" name="idTarifa" required />
                </div>
                <div>
                    <label for="nuevoValor">Nuevo valor por hora:</label>
                    <input type="text" id="nuevoValor" name="nuevoValor" required />
                </div>
                <button type="submit">Actualizar</button>
            </form>
        </c:otherwise>
    </c:choose>

</body>
</html>
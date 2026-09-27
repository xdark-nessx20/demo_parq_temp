<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Tarifas</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />
    <jsp:include page="/WEB-INF/views/comunes/mensajes.jsp" />

    <div class="page-head">
        <h2>Tarifas vigentes</h2>
        <div class="page-head-acciones">
            <a class="boton" href="${pageContext.request.contextPath}/tarifas?accion=registrar">Registrar nueva tarifa</a>
        </div>
    </div>

<jsp:include page="/WEB-INF/views/comunes/mensajes.jsp" />

    <c:choose>
        <c:when test="${empty tarifas}">
            <p>No hay tarifas registradas.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                    <tr>
                        <th>Tipo de vehículo</th>
                        <th>Valor por hora</th>
                        <th>Año vigencia</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="t" items="${tarifas}">
                        <tr>
                            <td>${tipoNombres[t.idTipoVehiculo]}</td>
                            <td>$${t.valorHoraTexto}</td>
                            <td>${t.anioVigencia}</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/tarifas" method="post"
                                      onsubmit="return confirm('¿Eliminar la tarifa de ${tipoNombres[t.idTipoVehiculo]} (${t.anioVigencia})?');">
                                    <input type="hidden" name="accion" value="eliminar" />
                                    <input type="hidden" name="idTarifa" value="${t.id}" />
                                    <button type="submit" class="boton-rojo boton-chico">Eliminar</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <h3>Actualizar precio</h3>
            <form action="${pageContext.request.contextPath}/tarifas" method="post">
                <input type="hidden" name="accion" value="actualizar" />
                <div>
                    <label for="idTarifa">Tarifa:</label>
                    <select id="idTarifa" name="idTarifa" required>
                        <option value="">-- Seleccione una tarifa --</option>
                        <c:forEach var="t" items="${tarifas}">
                            <option value="${t.id}">$${t.valorHoraTexto} / hora (${t.anioVigencia})</option>
                        </c:forEach>
                    </select>
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
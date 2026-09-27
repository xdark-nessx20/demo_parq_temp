<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mi cuenta</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <h1>Mi cuenta</h1>
    <p class="muted">Vehículos y movimientos de <strong>${sessionScope.usuario.nombre}</strong>.
        <span id="vivo" style="color:#16a34a;">●</span> <span class="muted">En vivo</span></p>

    <c:if test="${not empty sessionScope.mensaje}">
        <p class="mensaje mensaje-ok">${sessionScope.mensaje}</p>
        <c:remove var="mensaje" scope="session" />
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <p class="mensaje mensaje-error">${sessionScope.error}</p>
        <c:remove var="error" scope="session" />
    </c:if>

    <h2>Registrar mi vehículo</h2>
    <form action="${pageContext.request.contextPath}/mi-cuenta" method="post">
        <input type="hidden" name="accion" value="registrarVehiculo" />
        <div>
            <label for="placa">Placa:</label>
            <input type="text" id="placa" name="placa" placeholder="ABC-123 (carro) · ABC-12A (moto)" required />
        </div>
        <div>
            <label for="tipoVehiculo">Tipo de vehículo:</label>
            <select id="tipoVehiculo" name="tipoVehiculo" required>
                <option value="">-- Seleccione un tipo --</option>
                <c:forEach var="t" items="${tipos}">
                    <option value="${t.nombre}">${t.nombre}</option>
                </c:forEach>
            </select>
        </div>
        <button type="submit">Registrar vehículo</button>
    </form>

    <h2>Mis vehículos</h2>

    <c:choose>
        <c:when test="${empty movimientos}">
            <p>No tiene vehículos ni movimientos registrados.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                    <tr>
                        <th>Placa</th>
                        <th>Tipo</th>
                        <th>Entrada</th>
                        <th>Salida</th>
                        <th>Valor</th>
                        <th>Estado</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="m" items="${movimientos}">
                        <tr>
                            <td><span class="placa">${m.placa}</span></td>
                            <td>${m.tipo}</td>
                            <td>${m.horaEntrada}</td>
                            <td>${m.horaSalida}</td>
                            <td class="num">${m.valor}</td>
                            <td><span class="badge ${m.estado == 'Pagado' ? 'badge-ok' : (m.estado == 'Pendiente' ? 'badge-warn' : 'badge-info')}">${m.estado}</span></td>                            <td>
                                <c:if test="${m.pagable}">
                                    <form action="${pageContext.request.contextPath}/mi-cuenta" method="post"
                                          onsubmit="return confirm('¿Pagar ${m.valor}?');">
                                        <input type="hidden" name="idPago" value="${m.idPago}" />
                                        <button type="submit" class="boton-verde boton-chico">Pagar</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

    <h2>Mis datos</h2>
    <form action="${pageContext.request.contextPath}/mi-cuenta" method="post">
        <input type="hidden" name="accion" value="nombre" />
        <div>
            <label for="nombre">Nombre</label>
            <input type="text" id="nombre" name="nombre" value="${sessionScope.usuario.nombre}" required />
        </div>
        <button type="submit">Guardar</button>
    </form>

    <h2>Cambiar contraseña</h2>
    <form action="${pageContext.request.contextPath}/mi-cuenta" method="post">
        <input type="hidden" name="accion" value="contrasena" />
        <div>
            <label for="actual">Contraseña actual</label>
            <input type="password" id="actual" name="actual" required />
        </div>
        <div>
            <label for="nueva">Nueva contraseña</label>
            <input type="password" id="nueva" name="nueva" data-pw required />
        </div>
        <jsp:include page="/WEB-INF/views/comunes/reglas-password.jsp" />
        <button type="submit">Cambiar contraseña</button>
    </form>

    <jsp:include page="/WEB-INF/views/comunes/auto-refresco.jsp" />
</body>
</html>

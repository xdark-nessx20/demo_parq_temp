<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar tipo de vehículo</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <div class="page-head">
        <h2>Editar tipo de vehículo</h2>
        <div class="page-head-acciones">
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/tipos-vehiculo">Volver</a>
        </div>
    </div>

<jsp:include page="/WEB-INF/views/comunes/mensajes.jsp" />

    <form action="${pageContext.request.contextPath}/tipos-vehiculo" method="post">
        <input type="hidden" name="accion" value="editar" />
        <input type="hidden" name="id" value="${tipo.id}" />
        <div>
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" value="${tipo.nombre}" required />
        </div>
        <div>
            <label for="formatoPlaca">Formato de placa:</label>
            <select id="formatoPlaca" name="formatoPlaca" required>
                <option value="CARRO" ${tipo.formatoPlaca == 'CARRO' ? 'selected' : ''}>Carro — AAA-000</option>
                <option value="MOTO"  ${tipo.formatoPlaca == 'MOTO'  ? 'selected' : ''}>Moto — AAA-00A</option>
            </select>
        </div>
        <button type="submit">Guardar cambios</button>
    </form>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar tipo de vehículo</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/comunes/header.jsp" />

    <div class="page-head">
        <h2>Registrar tipo de vehículo</h2>
        <div class="page-head-acciones">
            <a class="boton boton-secundario" href="${pageContext.request.contextPath}/tipos-vehiculo">Volver</a>
        </div>
    </div>

    <c:if test="${not empty error}">
        <p style="color:red;"><strong>${error}</strong></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/tipos-vehiculo" method="post">

        <div>
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre"
                   value="${param.nombre}" required />
        </div>

        <div>
            <label for="formatoPlaca">Formato de placa (obligatorio):</label>
            <select id="formatoPlaca" name="formatoPlaca" required>
                <option value="">-- Seleccione el formato --</option>
                <option value="CARRO" ${param.formatoPlaca == 'CARRO' ? 'selected' : ''}>Carro — AAA-000</option>
                <option value="MOTO"  ${param.formatoPlaca == 'MOTO'  ? 'selected' : ''}>Moto — AAA-00A</option>
            </select>
            <small class="muted">Con esto el sistema valida las placas de este tipo (no se puede omitir).</small>
        </div>

        <button type="submit">Registrar</button>
    </form>

</body>
</html>
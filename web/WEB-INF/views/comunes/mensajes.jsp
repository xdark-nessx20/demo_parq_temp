<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${not empty sessionScope.mensaje}">
    <p class="mensaje mensaje-ok">${sessionScope.mensaje}</p>
    <c:remove var="mensaje" scope="session" />
</c:if>
<c:if test="${not empty sessionScope.error}">
    <p class="mensaje mensaje-error">${sessionScope.error}</p>
    <c:remove var="error" scope="session" />
</c:if>

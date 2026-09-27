<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--
  Muestra el mensaje (error/mensaje, de request o de sesion) como MODAL/OVERLAY
  con boton "Aceptar" (nada de mensajes inline en la pagina).
  Se incluye en cada vista; trae el modal y lo abre al cargar si hay mensaje.
--%>
<c:set var="msgEsError" value="${not empty error or (empty mensaje and not empty sessionScope.error)}" />
<c:set var="msgTexto" value="${not empty error ? error
        : (not empty mensaje ? mensaje
        : (not empty sessionScope.error ? sessionScope.error : sessionScope.mensaje))}" />

<c:if test="${not empty msgTexto}">
    <span id="flashTexto" hidden><c:out value="${msgTexto}"/></span>
    <jsp:include page="/WEB-INF/views/comunes/modal-mensaje.jsp" />
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            var t = document.getElementById('flashTexto');
            if (t && t.textContent.trim() && window.mostrarMensaje) {
                window.mostrarMensaje('${msgEsError ? "error" : "ok"}', t.textContent.trim());
            }
        });
    </script>
</c:if>

<c:remove var="error" scope="session" />
<c:remove var="mensaje" scope="session" />

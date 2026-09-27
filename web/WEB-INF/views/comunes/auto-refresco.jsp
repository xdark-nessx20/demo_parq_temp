<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Auto-refresco "en vivo": recarga la pagina cuando el estado cambia.
  El controlador debe dejar en el request:
    - firma        estado actual (texto)
    - refrescoUrl  endpoint ligero, p.ej. "/pagos?accion=estado"
  Se consulta cada 2 segundos y solo recarga si la firma cambio.
--%>
<script>
    (function () {
        var firmaActual = "${firma}";
        var url = "${pageContext.request.contextPath}${refrescoUrl}";

        setInterval(function () {
            // No recargar mientras el usuario esta escribiendo en un campo.
            var foco = document.activeElement;
            if (foco && /^(INPUT|SELECT|TEXTAREA)$/.test(foco.tagName)) return;

            // No recargar mientras se esta procesando un pago (modal abierto).
            var modal = document.getElementById('modalPago');
            if (modal && modal.classList.contains('visible')) return;

            fetch(url)
                .then(function (r) { return r.text(); })
                .then(function (t) { if (t !== firmaActual) { window.location.reload(); } })
                .catch(function () {});
        }, 2000);
    })();
</script>

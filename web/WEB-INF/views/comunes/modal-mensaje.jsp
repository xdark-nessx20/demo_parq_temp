<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Modal de mensaje (exito / advertencia / error).
  Se muestra con window.mostrarMensaje(tipo, texto) donde tipo es 'ok', 'warn' o 'error'.
  Reutiliza los estilos de .modal-overlay (el mismo del modal de pago).
--%>
<div class="modal-overlay" id="modalMensaje">
    <div class="modal-mensaje" id="modalMensajeCaja" role="alertdialog" aria-live="assertive">
        <div class="mm-icono" id="modalMensajeIcono"></div>
        <p id="modalMensajeTexto"></p>
        <button type="button" class="boton" id="modalMensajeCerrar">Entendido</button>
    </div>
</div>
<script>
    (function () {
        var overlay = document.getElementById('modalMensaje');
        if (!overlay) return;

        function cerrar() {
            overlay.classList.remove('abierto');
            setTimeout(function () { overlay.classList.remove('visible'); }, 250);
        }

        window.mostrarMensaje = function (tipo, texto) {
            document.getElementById('modalMensajeCaja').className = 'modal-mensaje es-' + tipo;
            document.getElementById('modalMensajeTexto').textContent = texto;
            overlay.classList.add('visible');
            void overlay.offsetWidth;              // fuerza reflujo para animar la entrada
            overlay.classList.add('abierto');
            document.getElementById('modalMensajeCerrar').focus();
        };

        document.getElementById('modalMensajeCerrar').addEventListener('click', cerrar);
        overlay.addEventListener('click', function (e) { if (e.target === overlay) cerrar(); });
        document.addEventListener('keydown', function (e) {
            if (e.key === 'Escape' && overlay.classList.contains('visible')) cerrar();
        });
    })();
</script>

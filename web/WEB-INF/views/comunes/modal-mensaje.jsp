<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Modal de mensaje (exito / advertencia / error).
  Se muestra con window.mostrarMensaje(tipo, texto) donde tipo es 'ok', 'warn' o 'error'.
  Reutiliza los estilos de .modal-overlay (el mismo del modal de pago).
--%>
<div class="modal-overlay" id="modalMensaje">
    <div class="modal-mensaje" id="modalMensajeCaja" role="alertdialog" aria-live="assertive">
        <div class="mm-icono" id="modalMensajeIcono"></div>
        <h3 class="mm-titulo" id="modalMensajeTitulo" hidden></h3>
        <p id="modalMensajeTexto"></p>
        <button type="button" class="boton" id="modalMensajeCerrar">Aceptar</button>
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

        // mostrarMensaje(tipo, texto[, titulo]): titulo es opcional.
        window.mostrarMensaje = function (tipo, texto, titulo) {
            document.getElementById('modalMensajeCaja').className = 'modal-mensaje es-' + tipo;

            var tituloEl = document.getElementById('modalMensajeTitulo');
            if (titulo) {
                tituloEl.textContent = titulo;
                tituloEl.hidden = false;
            } else {
                tituloEl.textContent = '';
                tituloEl.hidden = true;
            }

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

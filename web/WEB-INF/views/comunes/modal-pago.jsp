<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Modal de pago: cualquier form con data-pago se intercepta, muestra este modal
  3 segundos (con animacion de entrada y salida) y luego procesa el pago.
--%>
<div class="modal-overlay" id="modalPago">
    <div class="modal-pago" role="status" aria-live="polite">
        <h3>Procesando el pago</h3>
        <p>No hay presupuesto para una pasarela de pagos. Se procesará tu pago por arte de magia.</p>
        <div class="puntos"><span></span><span></span><span></span></div>
    </div>
</div>
<script>
    (function () {
        var overlay = document.getElementById('modalPago');
        if (!overlay) return;

        document.querySelectorAll('form[data-pago]').forEach(function (form) {
            form.addEventListener('submit', function (e) {
                if (form.dataset.procesando) return;   // ya se mostro el modal: dejar pasar
                e.preventDefault();

                overlay.classList.add('visible');
                void overlay.offsetWidth;              // fuerza reflujo para que anime la entrada
                overlay.classList.add('abierto');

                setTimeout(function () {
                    overlay.classList.remove('abierto');   // animacion de salida
                    setTimeout(function () {
                        overlay.classList.remove('visible');
                        form.dataset.procesando = '1';
                        form.submit();
                    }, 260);
                }, 3000);
            });
        });
    })();
</script>

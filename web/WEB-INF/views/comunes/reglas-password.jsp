<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="pw-req" id="pwReq">
    <span data-req="len">Mínimo 8 caracteres</span>
    <span data-req="upper">Al menos 1 mayúscula</span>
    <span data-req="symbol">Al menos 1 símbolo (!@#$…)</span>
</div>
<script>
    (function () {
        var input = document.querySelector('input[data-pw]');
        var box = document.getElementById('pwReq');
        if (!input || !box) return;
        var reglas = {
            len: function (v) { return v.length >= 8; },
            upper: function (v) { return /[A-Z]/.test(v); },
            symbol: function (v) { return /[^A-Za-z0-9]/.test(v); }
        };
        function pintar() {
            var v = input.value;
            Object.keys(reglas).forEach(function (k) {
                var el = box.querySelector('[data-req="' + k + '"]');
                if (el) el.className = reglas[k](v) ? 'ok' : '';
            });
        }
        input.addEventListener('input', pintar);
        pintar();
    })();
</script>

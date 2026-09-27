package com.parqueamestapp.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

// Formato de presentacion (montos con separador de miles, p.ej. 1.800).
public final class Formato {
    private Formato() {
    }

    public static String moneda(BigDecimal valor) {
        if (valor == null) return "";
        var nf = NumberFormat.getNumberInstance(new Locale("es", "CO"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(valor);
    }
}

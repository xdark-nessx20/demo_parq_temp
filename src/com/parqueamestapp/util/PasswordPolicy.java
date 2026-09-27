package com.parqueamestapp.util;

// Politica de contrasenas: minimo 8 caracteres, al menos 1 mayuscula y 1 simbolo.
public final class PasswordPolicy {
    private PasswordPolicy() {
    }

    public static boolean valida(String contrasena) {
        if (contrasena == null || contrasena.length() < 8) return false;
        boolean mayuscula = false;
        boolean simbolo = false;
        for (char c : contrasena.toCharArray()) {
            if (Character.isUpperCase(c)) mayuscula = true;
            else if (!Character.isLetterOrDigit(c)) simbolo = true;
        }
        return mayuscula && simbolo;
    }
}

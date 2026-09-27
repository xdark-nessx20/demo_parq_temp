package com.parqueamesta.persistence.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

// Hasheo de contraseñas con PBKDF2-HMAC-SHA256 (incluido en la JDK, sin librerias extra).
// Formato almacenado: "saltBase64:hashBase64"
public final class PasswordHasher {
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 210_000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;

    private PasswordHasher() {
    }

    public static String hash(String contrasenaPlano) {
        var salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        var hash = pbkdf2(contrasenaPlano.toCharArray(), salt);
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verificar(String contrasenaPlano, String almacenado) {
        if (contrasenaPlano == null || almacenado == null || !almacenado.contains(":")) return false;
        var partes = almacenado.split(":", 2);
        var salt = Base64.getDecoder().decode(partes[0]);
        var hashEsperado = Base64.getDecoder().decode(partes[1]);
        var hashCalculado = pbkdf2(contrasenaPlano.toCharArray(), salt);
        return tiempoConstante(hashEsperado, hashCalculado);
    }

    private static byte[] pbkdf2(char[] contrasena, byte[] salt) {
        try {
            var spec = new PBEKeySpec(contrasena, salt, ITERATIONS, KEY_BITS);
            return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    // Comparacion en tiempo constante (evita ataques de timing).
    private static boolean tiempoConstante(byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        int diff = 0;
        for (int i = 0; i < a.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}

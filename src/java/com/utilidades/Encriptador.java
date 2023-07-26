/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utilidades;

/**
 *
 * @author JCMM
 */
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Encriptador {

    private static final String SECRET_KEY = "mi_clave_secreta";
    private static final String SALT = "mi_sal_aleatoria";

    public static String encriptar(String texto) throws Exception {
        SecretKeySpec secretKeySpec = generarClaveSecreta();
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(texto.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String desencriptar(String textoEncriptado) throws Exception {
        SecretKeySpec secretKeySpec = generarClaveSecreta();
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = Base64.getDecoder().decode(textoEncriptado);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    private static SecretKeySpec generarClaveSecreta() throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    public static void main(String[] args) throws Exception {
        String textoOriginal = "Texto a encriptar";
        String textoEncriptado = encriptar(textoOriginal);
        System.out.println("Texto encriptado: " + textoEncriptado);
        String textoDesencriptado = desencriptar(textoEncriptado);
        System.out.println("Texto desencriptado: " + textoDesencriptado);
    }
}

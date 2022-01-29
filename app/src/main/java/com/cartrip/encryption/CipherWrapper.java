package com.cartrip.encryption;

import android.content.Context;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;

/**
 * A wrapper for a {@link Cipher} to handle encrypt and decrypt operations of string.
 */
public class CipherWrapper {
    private static final String AES_MODE = "AES/GCM/NoPadding";
    private static final int GCM_NONCE_LENGTH = 12; // in bytes
    private static final int GCM_TAG_LENGTH = 16; // in bytes

    private byte[] tag;
    private GCMParameterSpec spec;

    public byte[] encryptData(Key key, String inputData) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(AES_MODE);

        SecureRandom secureRandom = new SecureRandom();
        final byte[] nonce = new byte[GCM_NONCE_LENGTH];
        secureRandom.nextBytes(nonce);
        spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        tag = "World".getBytes(StandardCharsets.UTF_8);
        cipher.updateAAD(tag);

        byte[] cipherText = cipher.doFinal(inputData.getBytes(StandardCharsets.UTF_8));
        return cipherText;
    }

    // FIXME call decryptData with a prior call to encryptData??
    public String decryptData(Key key, byte[] inputDataEnc) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        cipher.updateAAD(tag);
        byte[] plainText = cipher.doFinal(inputDataEnc);
        return new String(plainText);
    }
}

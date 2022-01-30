package com.cartrip.encryption;

import static org.junit.Assert.*;

import android.util.Log;

import org.junit.Test;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;

public class CipherWrapperTest {
    public static final int AES_KEY_SIZE = 128; // in bits

    @Test
    public void testEncryptDecryptData() throws GeneralSecurityException {
        String plainText = "test";

        SecureRandom secureRandom = new SecureRandom(); //SecureRandom.getInstanceStrong();
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(AES_KEY_SIZE, secureRandom);
        Key key = keyGenerator.generateKey();

        CipherWrapper cipherWrapper = new CipherWrapper();
        byte[] encData = cipherWrapper.encryptData(key, plainText);
        String resultPlainText = cipherWrapper.decryptData(key, encData);

        assertEquals(plainText, resultPlainText);
    }

    @Test
    public void testDecryptWithoutEncryptCall() {
        // TODO
    }
}
package org.pol.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class Hash256 {
    private static final String SHA_256 = "SHA-256";
    private MessageDigest digest;

    public Hash256() throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance(SHA_256);
    }

    public String hash(String ... data) {
        String value = String.join("#", Arrays.asList(data));
        return Base64.getEncoder().encodeToString(hash(value.getBytes(StandardCharsets.UTF_8)));
    }

    public byte[] hash(byte[] data) {
        return digest.digest(data);
    }

}

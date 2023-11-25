package io.task.api.app.utils;

import java.security.Key;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AlgoAES {

    @Value("${application.api.encryption: randomChars}")
    private final String secretKey = "yvsE`6Z8cJITGDwAd7HM:QL\\?j;my1ed";
    private final String ALGORITHM = "AES";
    private final byte[] keyValue = secretKey.getBytes();

    private Key generateKey() throws Exception {
        return new SecretKeySpec(keyValue, ALGORITHM);
    }

    public String encrypt(String valueToEnc) {
        String enrypted = "";
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, this.generateKey());

            byte[] encValue = cipher.doFinal(valueToEnc.getBytes());
            byte[] encryptedByteValue = new Base64().encode(encValue);

            enrypted = new String(encryptedByteValue);

        } catch (Exception e) {
            throw new DataNotDecryptedException(e.getMessage());
        }
        return enrypted;
    }

    public String decrypt(String encryptedValue) {

        String decrypted = "";
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, this.generateKey());

            byte[] decodedBytes = new Base64().decode(encryptedValue.getBytes());
            byte[] enctVal = cipher.doFinal(decodedBytes);

            decrypted = new String(enctVal);

        } catch (Exception e) {
            throw new DataNotDecryptedException(e.getMessage());

        }
        return decrypted;

    }

    static String getAlphaNumericString(int n) {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = n;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

}

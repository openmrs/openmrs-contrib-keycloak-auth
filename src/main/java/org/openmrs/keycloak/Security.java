package org.openmrs.keycloak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {
    //TODO I have replaced all the APIException to RuntimeException
    private static final Logger log = LoggerFactory.getLogger(Security.class);

    public static boolean hashMatches(String hashedPassword, String passwordToHash) {
        if (hashedPassword == null || passwordToHash == null) {
            throw new RuntimeException("password.cannot.be.null");
        }

        return hashedPassword.equals(encodeString(passwordToHash))
                || hashedPassword.equals(encodeStringSHA1(passwordToHash))
                || hashedPassword.equals(incorrectlyEncodeString(passwordToHash));
    }

    public static String encodeString(String strToEncode) throws RuntimeException {
        String algorithm = "SHA-512";
        MessageDigest md;
        byte[] input;
        try {
            md = MessageDigest.getInstance(algorithm);
            input = strToEncode.getBytes(StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            // Yikes! Can't encode password...what to do?
            log.error(getPasswordEncodeFailMessage(algorithm), e);
            throw new RuntimeException("system.cannot.find.password.encryption.algorithm");
        }

        return hexString(md.digest(input));
    }

    private static String getPasswordEncodeFailMessage(String algo) {
        return "Can't encode password because the given algorithm: " + algo + " was not found! (fail)";
    }

    private static String encodeStringSHA1(String strToEncode) throws RuntimeException {
        String algorithm = "SHA1";
        MessageDigest md;
        byte[] input;
        try {
            md = MessageDigest.getInstance(algorithm);
            input = strToEncode.getBytes(StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            // Yikes! Can't encode password...what to do?
            log.error(getPasswordEncodeFailMessage(algorithm), e);
            throw new RuntimeException("system.cannot.find.encryption.algorithm");
        }

        return hexString(md.digest(input));
    }

    private static String incorrectlyEncodeString(String strToEncode) throws RuntimeException {
        String algorithm = "SHA1";
        MessageDigest md;
        byte[] input;
        try {
            md = MessageDigest.getInstance(algorithm);
            input = strToEncode.getBytes(StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            // Yikes! Can't encode password...what to do?
            log.error(getPasswordEncodeFailMessage(algorithm), e);
            throw new RuntimeException("system.cannot.find.encryption.algorithm");
        }

        return incorrectHexString(md.digest(input));
    }

    private static String hexString(byte[] block) {
        StringBuilder buf = new StringBuilder();
        char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        int high;
        int low;
        for (byte aBlock : block) {
            high = ((aBlock & 0xf0) >> 4);
            low = (aBlock & 0x0f);
            buf.append(hexChars[high]);
            buf.append(hexChars[low]);
        }

        return buf.toString();
    }

    private static String incorrectHexString(byte[] b) {
        if (b == null || b.length < 1) {
            return "";
        }
        StringBuilder s = new StringBuilder();
        for (byte aB : b) {
            s.append(Integer.toHexString(aB & 0xFF));
        }
        return new String(s);
    }
}

//package com.example.DEMO_INTEGRATION.Utils.ChecksumUtil;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//
//public class HashUtils {
//
//    private static final String HMAC_ALGORITHM = "HmacSHA256";
//
//    public static String nrlmcalculateHMAC(byte[] data, String key) {
//        try {
//            String base64EncodedData = Base64.getEncoder().encodeToString(data);
//            System.err.println(base64EncodedData);
//            Mac hmac = Mac.getInstance(HMAC_ALGORITHM);
//            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
//            hmac.init(secretKey);
//            byte[] hmacBytes = hmac.doFinal(base64EncodedData.getBytes(StandardCharsets.UTF_8));
//
//            return nrlmbytesToHex(hmacBytes);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to calculate HMAC", e);
//        }
//    }
//
//    private static String nrlmbytesToHex(byte[] bytes) {
//        StringBuilder hexString = new StringBuilder(2 * bytes.length);
//        for (byte b : bytes) {
//            String hex = Integer.toHexString(0xff & b);
//            if (hex.length() == 1) {
//                hexString.append('0');
//            }
//            hexString.append(hex);
//        }
//        return hexString.toString();
//    }
//}

package com.example.myapplication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Configuration {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final String STRING_TODAY = LocalDate.now().format(Configuration.FORMATTER);

    // MD5 hashed
    public static String md5(String input) {
        try {
            // Lấy đối tượng MessageDigest với thuật toán MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            // Băm chuỗi đầu vào và trả về mảng byte
            byte[] bytes = digest.digest(input.getBytes());

            // Chuyển đổi mảng byte thành chuỗi hex
            StringBuilder hexString = new StringBuilder();
            for (byte b : bytes) {
                // Chuyển đổi từng byte thành chuỗi hex và thêm vào kết quả
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // Trả về chuỗi hex
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Nếu thuật toán không tồn tại, trả về null
            e.printStackTrace();
            return null;
        }
    }
}


package com.example.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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

    public static String randomString(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length);
    }

    public static String makeUsername(String name, String phone) {
        String[] words = name.split(" ");
        String lastWord = words[words.length - 1];
        String lastname = Normalizer.normalize(lastWord, Normalizer.Form.NFD).toLowerCase();
        return phone+lastname;
    }

    public static void sendMail(Context context, String to, String subject, String content) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String from = "testmailjava2468@gmail.com";
                String password = "poorzvdwovcmludd";

                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP HOST
                props.put("mail.smtp.port", "587"); // TSL
                props.put("mail.smtp.auth", "true"); // Yêu cầu đăng nhập
                props.put("mail.smtp.starttls.enable", "true"); // Start TLS

                Authenticator auth = new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                };

                Session session = Session.getInstance(props, auth);

                try {
                    MimeMessage msg = new MimeMessage(session);
                    msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
                    msg.setFrom(from);
                    msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
                    msg.setSubject(subject);
                    msg.setSentDate(new Date());
                    msg.setContent(content, "text/html; charset=UTF-8");

                    Transport.send(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void showDialog(Dialog dialog, int layout) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        dialog.setCancelable(true); // có thể bấm ra ngoài để đóng dialog

        Window window = dialog.getWindow();
        if(window == null) return;

        // Set kích thước và màu nền
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Set gravity center
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.show();
    }

}


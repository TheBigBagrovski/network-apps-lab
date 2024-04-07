package org.lab3;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Properties;

public class SimpleEmailClient {
    public static void main(String[] args) {
        // Параметры SMTP-сервера
        String host = "smtp.gmail.com";
        String port = "587";
        String username = ""; // Ваш адрес электронной почты
        String password = ""; // Ваш пароль

        // Параметры письма
        String toAddress = ""; // Адрес получателя
        String subject = "Test subject";
        String bodyText = "Hello, this is test email with an image";

        // Подготовка свойств для подключения
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Создание аутентификатора
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        // Создание сессии
        Session session = Session.getInstance(props, auth);

        try {
            // Создание объекта сообщения
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            message.setSubject(subject);

            // Создание тела сообщения с текстом и изображением
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(bodyText);

            // Путь к изображению
            String imagePath = "src\\main\\java\\org\\lab3\\img.jpg";

            MimeBodyPart imagePart = new MimeBodyPart();
            imagePart.attachFile(imagePath);

            // Создание многочастного сообщения
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(imagePart);

            // Установка многочастного сообщения в качестве содержимого письма
            message.setContent(multipart);

            // Отправка письма
            Transport.send(message);

            System.out.println("Письмо успешно отправлено.");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
}

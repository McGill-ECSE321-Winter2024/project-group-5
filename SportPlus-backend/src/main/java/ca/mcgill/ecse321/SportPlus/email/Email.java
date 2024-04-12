package ca.mcgill.ecse321.SportPlus.email;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * This class provides methods to send emails using JavaMail API.
 */
public class Email {

    private static String emailFrom = "sportplus.noreplyemail@gmail.com";
    private static String appPass = "zetc hdrk wkcq fnrn";

    /**
     * Sends an email.
     * 
     * @param send    Indicates whether to send the email ("send" to send the email,
     *                any other value to skip)
     * @param to      The recipient's email address
     * @param subject The subject of the email
     * @param text    The content of the email
     */
    public static void sendEmail(String send, String to, String subject, String text) {
        if (send == "send") {
            Properties properties = System.getProperties();

            properties.setProperty("mail.smtp.host", "smtp.gmail.com");
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.port", "587");
            properties.setProperty("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailFrom, appPass);
                }
            });

            try {
                MimeMessage message = new MimeMessage(session);

                message.setFrom(new InternetAddress(emailFrom));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                message.setSubject(subject);
                message.setText(text);

                Transport.send(message);
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }
        }
    }
}

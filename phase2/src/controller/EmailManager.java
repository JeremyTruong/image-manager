package controller;

import model.Picture;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/** The EmailManager class contains a method to send an email containing a Picture. */
public class EmailManager {
  /**
   * Sends an email of a Picture. The senderName argument is the name of the sender. The recipient
   * argument is the email address of the recipient. The picture argument is the Picture that the
   * sender wants to include in the email. Handles AddressException when the recipient is not a
   * valid email address. Also handles MessagingException when the email components cannot be
   * understood. Code based on: https://www.tutorialspoint.com/java/java_sending_email.htm
   *
   * @param senderName the name of the sender
   * @param recipient the email address of the recipient
   * @param picture the Picture the user wishes to send
   */
  public static void emailPicture(String senderName, String recipient, Picture picture) {
    Properties mailServerProperties;
    Session getMailSession;
    MimeMessage generateMailMessage;
    try {
      mailServerProperties = System.getProperties();
      mailServerProperties.put("mail.smtp.port", "587");
      mailServerProperties.put("mail.smtp.auth", "true");
      mailServerProperties.put("mail.smtp.starttls.enable", "true");

      getMailSession = Session.getDefaultInstance(mailServerProperties, null);
      generateMailMessage = new MimeMessage(getMailSession);
      generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
      generateMailMessage.setSubject("You have a new image from: " + senderName);

      // attach image
      String file = picture.getLocation().toString();
      DataSource source = new FileDataSource(file);
      generateMailMessage.setDataHandler(new DataHandler(source));
      generateMailMessage.setFileName(picture.getNameWithExtension());

      Transport transport = getMailSession.getTransport("smtp");
      transport.connect("smtp.gmail.com", "group0429@gmail.com", ")%ZkqvMR'U\"V[2+R");
      transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
      transport.close();
    } catch (AddressException e) {
      e.printStackTrace();
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
}

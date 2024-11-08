package com.amstelcrete.mail;

import com.amstelcrete.config.Credentials;
import com.amstelcrete.error.GenericException;
import jakarta.mail.Address;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sending mails to the relay server.
 */
public class MailService {

  private static final Logger log = LoggerFactory.getLogger(MailService.class);

  private static final String TEXT_HTML = "text/html";

  private final Properties config;

  public MailService() {
    this.config = getMailProperties("/mail.properties");
  }

  /**
   * Get the non confidentail mail properties.
   *
   * @param fileName - name of the properties file
   * @return the loaded non confidential properties
   */
  private Properties getMailProperties(String fileName) {
    Properties cfg = new Properties();
    try (InputStream is = getClass().getResourceAsStream(fileName)) {
      cfg.load(is);
    } catch (IOException e) {
      log.error(String.format("Could not obtain '%s' from classpath", fileName), e);
      throw new GenericException(e);
    }
    return cfg;
  }

  /**
   * Send the mail to the relay server.
   *
   * @param mailRecord - subject and HTML-content of the mail
   */
  public void sendMail(MailRecord mailRecord) {
    Session session = getSession();
    MimeMessage message = new MimeMessage(session);
    try {
      message.setFrom(config.getProperty("mail.from"));
      Address[] recipients = {new InternetAddress(config.getProperty("mail.to"))};
      message.setRecipients(Message.RecipientType.TO, recipients);
      message.setSubject(mailRecord.subject());
      message.setContent(mailRecord.content(), TEXT_HTML);
      Transport.send(message);
    } catch (MessagingException e) {
      log.error("Failed to send e-mail", e);
      throw new GenericException(e);
    }
  }


  private Session getSession() {
    Properties props = new Properties();
    props.put("mail.smtp.host", config.getProperty("mail.host"));
    props.put("mail.smtp.port", config.getProperty("mail.port"));
    props.put("mail.smtp.auth", config.getProperty("mail.smtp.auth", "false"));
    props.put("mail.smtp.starttls.enable",
        config.getProperty("mail.smtp.starttls.enable", "false"));
    Authenticator auth = new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(Credentials.getProperty("mail.user"),
            Credentials.getProperty("mail.password"));
      }
    };
    return Session.getInstance(props, auth);
  }



}

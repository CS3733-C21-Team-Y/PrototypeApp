package edu.wpi.cs3733.c21.teamY.dataops;

import org.apache.commons.mail.*;

public class EmailUtils {

  public static void sendEmail(
      String fromEmail, String toEmail, String withSubject, String andMessage) {
    try {
      Email email = new SimpleEmail();

      // Configuration
      email.setHostName("smtp.office365.com");
      email.setSslSmtpPort("587");
      email.setSmtpPort(587);
      email.setStartTLSEnabled(true);
      email.setAuthenticator(new DefaultAuthenticator("test@edit2014.com", "yzbgqyrtkcnjgpnl")); //

      // Required for gmail
      email.setSSLOnConnect(false);

      // Sender
      email.setFrom(fromEmail);

      // Email title
      email.setSubject(withSubject);

      // Email message.
      email.setMsg(andMessage);

      // Receiver
      email.addTo(toEmail);
      email.send();
      System.out.println("Sent!!");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

package edu.wpi.cs3733.c21.teamY.dataops;

import org.apache.commons.mail.*;

public class EmailUtils implements Runnable {
  private Thread thread;
  private String fromEmail;
  private String toEmail;
  private String withSubject;
  private String andMessage;
  private String threadName;

  public EmailUtils(
      String fromEmail, String toEmail, String withSubject, String andMessage, String threadname) {
    this.fromEmail = fromEmail;
    this.toEmail = toEmail;
    this.withSubject = withSubject;
    this.andMessage = andMessage;
    this.threadName = threadname;
  }

  @Override
  public void run() {
    System.out.println("Running Thread: " + this.threadName);

    try {
      sendEmail();
    } catch (Exception e) {
      System.out.println("Thread Interrupted: " + this.threadName);
    }

    System.out.println("Exiting Thread: " + this.threadName);
  }

  public void start() {
    if (thread == null) {
      thread = new Thread(this, this.threadName);
      thread.start();
    }
  }

  private void sendEmail() {
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

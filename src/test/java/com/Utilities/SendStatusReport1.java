package com.Utilities;

import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SendStatusReport1 {

	private String from;
	private String to;
	private String subject;
	private String messageBody;
	private String fileName;
	private String host;

	private Properties properties;

	private MimeMessage message;
	private BodyPart messageBodyPart;
	private Multipart multipart;

	private Authenticator authenticator;

	public SendStatusReport1 () {
		from = "jenkinslogixal@logixal.com";
		to = "rohit.karkhanis@logixal.com";
		subject = "Automation Test execution Status";
		messageBody = "<html><body>Automation test execution status for Build#$BUILD_NUMBER is [$BUILD_STATUS]\r\n" + 
				"\r\n" + "Please find attached report for details.</body></html>";
		fileName = "STMExtentReport_KAMAN.html";
		host = "smtp.gmail.com";

		authenticator = new SMTPAuthenticator ();
		properties = System.getProperties ();
		properties.put ( "mail.smtp.host", host );
		properties.put ( "mail.smtp.starttls.enable", "true" );
		properties.put ( "mail.smtp.port", "587" );
		properties.put ( "mail.smtp.auth", "true" );
		properties.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
	}

	private void sendMail ( String from, String to,
			String subject, String messageBody, String fileName ) {
		try {
			Session session = Session.getDefaultInstance ( properties, authenticator );
			message = new MimeMessage ( session );
			message.setFrom ( new InternetAddress ( from ) );
			message.addRecipient ( Message.RecipientType.TO,
					new InternetAddress ( to ) );
			message.setSubject ( subject );

			multipart = new MimeMultipart ();
			messageBodyPart = new MimeBodyPart ();
			messageBodyPart.setContent ( messageBody, "text/html" );
			multipart.addBodyPart ( messageBodyPart );

			messageBodyPart = new MimeBodyPart ();
			DataSource source = new FileDataSource ( "./test-output/" + fileName );
			messageBodyPart.setDataHandler ( new DataHandler ( source ) );
			messageBodyPart.setFileName ( fileName );
			multipart.addBodyPart ( messageBodyPart );

			message.setContent ( multipart );

			Transport.send ( message );
			System.out.println ( "Message send successfully...." );
		} catch ( Exception me ) {
			me.printStackTrace ();
		}
	} 

	public void performTask () {
		sendMail ( from, to, subject, messageBody, fileName );
	}

	public static void main ( String[] args ) {
		new SendStatusReport1 ().performTask ();
	}
}

/**
 * SimpleAuthenticator is used to do simple authentication
 * when the SMTP server requires it.
 */

class SMTPAuthenticator extends Authenticator {

	private static final String SMTP_AUTH_USER = "jenkinslogixal@gmail.com";
	private static final String SMTP_AUTH_PASSWORD = "Test@123";

	public PasswordAuthentication getPasswordAuthentication () {
		String username = SMTP_AUTH_USER;
		String password = SMTP_AUTH_PASSWORD;

		return new PasswordAuthentication( username,  password );
	}
}
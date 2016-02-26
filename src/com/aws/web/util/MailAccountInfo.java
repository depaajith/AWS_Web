package com.aws.web.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailAccountInfo {
	public static final String M_PROTOCOL = "";
	public static final String M_PORT = "";
	public static final String M_HOST = "";
	public static String username = "";
	public static String pwd = "";

	public static class MailAuthenticator extends Authenticator {
		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, pwd);
		}
	}

	public void invAccountInfo(String subject, String sMesgTrap,
			String toAddress) throws Exception {
		String fromName = "Info", fromAddress = "jgeorge@acr.org";

		String sTime = "", sDay = "", sZone = "", sBodyText = "";
		Session session = null;
		try {
			Calendar today = new GregorianCalendar();
			sTime = today.getTime().toString();
			sDay = Integer.toString(today.DATE);
			sZone = today.getTimeZone().getDisplayName();

			sBodyText = "This information E-mail is Auto generated by the application server "
					+ " at " + sTime + " (" + sZone + ").\n\n";

			// Get System properties
			Properties props = System.getProperties();
			// Setup mail server
			props.put("mail.smtp.host", M_HOST);
			props.put("mail.smtp.auth", "true");

			// Get session
			session = Session.getInstance(props, new MailAuthenticator());
			// Define message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress, fromName));

			InternetAddress[] toIAddress = InternetAddress.parse(toAddress);
			message.addRecipients(Message.RecipientType.TO, toIAddress);
			message.addRecipients(Message.RecipientType.BCC, fromAddress);

			message.setSubject(subject);

			sBodyText = sMesgTrap + "\n \n" + sBodyText;
			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(sBodyText);
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			message.setSubject(subject);
			message.setContent(multipart);

			message.saveChanges(); // implicit with send()
			Transport transport = session.getTransport("smtp");
			transport.connect(M_HOST, username, pwd);
			// Send the message
			transport.sendMessage(message, message.getAllRecipients());
			// Close the Connection
			transport.close();

		} catch (Exception ex) {
			System.out.println("Exception Raised in Invoked mailAccountInfo \n"
					+ ex.toString());
			return;
		}
		return;
	}
}

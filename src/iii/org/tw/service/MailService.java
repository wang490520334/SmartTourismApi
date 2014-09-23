package iii.org.tw.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailService {

	//private static String username = "smarttourism001@gmail.com";
	//private static String password = "001smarttourism";
	private static String username = "vzTaiwan@iii.org.tw";
	private static String password = "Tourism6#";
	private static Properties props = new Properties();

	static {
		//props.put("mail.smtp.auth", "true");
		//props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.iii.org.tw");
		//props.put("mail.smtp.port", "465");
		props.put("mail.smtp.port", "25");
		//props.put("mail.smtp.ssl.enable", true);
		//props.put("mail.smtp.ssl.trust", "*");
	
	}
/*	
'mail' => array(
  'host' => 'mta1.iii.org.tw',
  'port' => '465',
  'ssl' => 'ssl',
  'username' => 'vzTaiwan@iii.org.tw',
  'password' => 'Tourism6#',
  'fromaddress' => 'vzTaiwan@iii.org.tw',
  'fromname' => 'III',
  'signupsubject' => 'Register for our VZ Taiwan',
  'body' => "Please click this link to activate your account \n\n",
),


	
*/	

	public static void sendMail(String subject, String body, String from, String to) {
		
		System.out.println("1");
		
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {
			System.out.println("2");

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(body);

			System.out.println("3");
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}

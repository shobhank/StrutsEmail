package org.shobhank;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.opensymphony.xwork2.ActionSupport;

public class MailAction extends ActionSupport {
	private String from;
	private String password;
	private String to;
	private String subject;
	private String body;
	private static final String emailRegex;
	static Properties properties = new Properties();
	private Pattern pattern;
	private Matcher matcher;
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	static{
		emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class",
	                     "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");
	}
	
	public String execute(){
		String result = "success";
		Session session = Session.getDefaultInstance(properties, 
				new Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication(){
				return new PasswordAuthentication(from,password);
			}		
		});
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(from));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			msg.setSubject(subject);
			msg.setText(body);
			Transport.send(msg);
		} catch (AddressException e) {
			result = "error";
			e.printStackTrace();
		} catch (MessagingException e) {
			result = "error";
			e.printStackTrace();
		}
		return result;
	}
	
	public void validate(){
		if (from.equals(""))
			addFieldError("from","The field is required");
		if (to.equals(""))
			addFieldError("to","The field is required");
		pattern = Pattern.compile(emailRegex);	
		matcher = pattern.matcher(from);
		if(!matcher.matches())
			addFieldError("from","The field has error please verify emailID");
		matcher = pattern.matcher(to);
		if(!matcher.matches())
			addFieldError("to","The field has error please verify emailID");
	}
}

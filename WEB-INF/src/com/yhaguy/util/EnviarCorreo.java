package com.yhaguy.util;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.yhaguy.domain.RegisterDomain;
import com.yhaguy.domain.Tarea_Programada;

public class EnviarCorreo {
	
	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_PORT = "587";
	private static final String SMTP_START_TLS_ENABLE = "true";
	private static final String EMAIL_FROM = "yhaguysys@gmail.com";
	private static final String EMAIL_FROM_PASSWORD = "yhaguyserversystem123";
	
	public static final String[] DESTINATARIO = new String[]{ "nataliac@yhaguyrepuestos.com.py" };
	public static final String[] COPIA_OCULTA = new String[]{ "sergioraul777@gmail.com" };
	
	private Tarea_Programada tarea;
	
	public EnviarCorreo() {
	}
	
	public EnviarCorreo(Tarea_Programada tarea) {
		this.tarea = tarea;
	}

	public void sendMessage(String[] recipients, String[] recipientsCCO, 
			String subject, String message, String fileName, String path)
			throws Exception {

		sendMessage(recipients, new String[] { "" }, recipientsCCO, subject, message, 
				EMAIL_FROM, EMAIL_FROM_PASSWORD,
				fileName, path);
	}

	public void sendMessage(String[] recipients, String[] recipientsCC,
			String[] recipientsCCO, String subject, String message,
			String from, String pass, String fileName, String path)
			throws Exception {
		boolean debug = false;
		Properties props = new Properties();

		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.starttls.enable", SMTP_START_TLS_ENABLE);
		props.put("mail.smtp.auth", "true");
		props.setProperty("proxySet","true");
        props.setProperty("socksProxyHost","10.25.5.1");
        props.setProperty("socksProxyPort","1080");
				
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EMAIL_FROM, EMAIL_FROM_PASSWORD);
			}
		});

		session.setDebug(debug);

		BodyPart mensaje = new MimeBodyPart();
		mensaje.setText(message);

		MimeMultipart multiParte = new MimeMultipart();
		multiParte.addBodyPart(mensaje);

		if (path != null) {
			BodyPart adjunto = new MimeBodyPart();
			adjunto.setDataHandler(new DataHandler(new FileDataSource(path)));
			adjunto.setFileName(fileName);
			multiParte.addBodyPart(adjunto);
		}

		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// verifica si destinatario CC no es vacio
		if (recipientsCC[0].isEmpty() == false) {

			InternetAddress[] addressToCC = new InternetAddress[recipientsCC.length];
			for (int i = 0; i < recipientsCC.length; i++) {
				addressToCC[i] = new InternetAddress(recipientsCC[i]);
			}
			msg.setRecipients(Message.RecipientType.CC, addressToCC);

		}

		// verifica si destinatario CCO no es vacio
		if (recipientsCCO[0].isEmpty() == false) {

			InternetAddress[] addressToCCO = new InternetAddress[recipientsCCO.length];
			for (int i = 0; i < recipientsCCO.length; i++) {
				addressToCCO[i] = new InternetAddress(recipientsCCO[i]);
			}
			msg.setRecipients(Message.RecipientType.BCC, addressToCCO);

		}

		// Configura el asunto y el tipo de contenido
		msg.setSubject(subject);
		msg.setContent(multiParte);
		
		try {
			Transport.send(msg);
			if (tarea != null) {
				RegisterDomain rr = RegisterDomain.getInstance();
				rr.saveObject(tarea, "sys");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}

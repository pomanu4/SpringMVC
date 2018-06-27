package ua.com.company.component;

import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import ua.com.company.entity.Person;
import ua.com.company.event.OnPersonRegistrationEvent;
import ua.com.company.serviceInterf.IavTokenService;

@Component
public class PersonRegistrationListener implements ApplicationListener<OnPersonRegistrationEvent> {
	
	@Autowired
	private IavTokenService tokService;

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void onApplicationEvent(OnPersonRegistrationEvent event) {
		this.confirmRegistration(event);
	}

	protected void confirmRegistration(OnPersonRegistrationEvent event) {

		Person person = event.getPerson();
		String token = UUID.randomUUID().toString();
		System.out.println(token);
		tokService.saveToken(token, person);
		String emailTo = "pomanu4@ukr.net";
		String subject = "confirm registration";
		String confirmUrl = event.getAppUrl() + "/registrationConfirm.html?token=" + token;

		SimpleMailMessage mesage = new SimpleMailMessage();
		mesage.setTo(emailTo);
		mesage.setSubject(subject);
		mesage.setText("you are registrated on some site, to activate account clic to this reference "
				+ "http://localhost:8080" + confirmUrl);
		mailSender.send(mesage);
	}

	@SuppressWarnings("unused")
	private void mimeMessageMethod() {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		try {
			helper.setFrom(new InternetAddress("pomanu42@gmail.com"));
			helper.setTo(new InternetAddress("pomanu4@ukr.net"));
			helper.setText("some text", true);

		} catch (MessagingException e) {
			System.out.println("exeption in mail sender method");
			e.printStackTrace();
		}
	}

}

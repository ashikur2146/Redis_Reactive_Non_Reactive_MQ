package reactive.email;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import reactive.redis.broker.pubsub.x.RedisMessagePublisher;

@Service
public class EmailServiceImpl implements EmailService<EmailInfo, String> {
	
	private static final Logger LOGGER = Logger.getLogger(EmailServiceImpl.class.getName());

	private static final String EMAIL = "EMAIL";
	private static final String PASSWORD = "PASSWORD";

	private RedisMessagePublisher<String, EmailInfo> redisMessagePublisher;
	private JavaMailSenderImpl javaMailSenderImpl;

	public EmailServiceImpl(RedisMessagePublisher<String, EmailInfo> redisMessagePublisher, JavaMailSenderImpl javaMailSenderImpl) {
		this.redisMessagePublisher = redisMessagePublisher;
		this.javaMailSenderImpl = javaMailSenderImpl;
	}

	@Override
	public String sendEmail(EmailInfo k) throws Exception {
		redisMessagePublisher.publishMessage(k);
		return "*** Email Sent successfully ***";
	}

	@Override
	public String sendEmailSync(EmailInfo emailInfo) {
		try {
			String recepient = emailInfo.recipient();
			String subject = emailInfo.subject();
			String messageBody = emailInfo.body();
			LOGGER.log(Level.INFO, "recepient: " + recepient + "; subject: " + subject + "; message body: " + messageBody);
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(recepient);
			message.setSubject(subject);
			message.setText(messageBody);
			message.setFrom(javaMailSenderImpl.getUsername());
			javaMailSenderImpl.send(message);
		} catch (MailException e) {
			throw new RuntimeException("Error sending email", e);
		}
		return "email Sent Successfully";
	}

	@PostConstruct
	private void loadSenderInfo() {
		LOGGER.log(Level.INFO, "<-- Mail sender info being loaded -->");
		final String sender = System.getenv(EMAIL);
		final String password = System.getenv(PASSWORD);
		LOGGER.log(Level.INFO, "sender: " + sender);
		this.javaMailSenderImpl.setUsername(sender);
		this.javaMailSenderImpl.setPassword(password);
		LOGGER.log(Level.INFO, "<-- Mail sender info loaded successfully -->");
	}
}
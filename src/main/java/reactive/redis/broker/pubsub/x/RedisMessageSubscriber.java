package reactive.redis.broker.pubsub.x;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactive.email.EmailInfo;
import reactive.email.EmailService;

@Component
@Lazy
public class RedisMessageSubscriber implements MessageListener {

	private static final Logger LOGGER = Logger.getLogger(RedisMessageSubscriber.class.getName());

	@Value("${spring.redis.channel.topic.X}")
	private String emailChannel;

	private EmailService<EmailInfo, String> emailService;

	public RedisMessageSubscriber(EmailService<EmailInfo, String> emailService) {
		this.emailService = emailService;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		LOGGER.log(Level.INFO, "Received message: " + message);
		String channel = new String(message.getChannel(), java.nio.charset.StandardCharsets.UTF_8);
		String body = new String(message.getBody(), java.nio.charset.StandardCharsets.UTF_8);
		LOGGER.log(Level.INFO, "channel: " + channel);
		LOGGER.log(Level.INFO, "message body: " + body);
		if (channel.equals(this.emailChannel)) {
			LOGGER.log(Level.INFO, "Channel validation successful!");
			try {
				Optional<EmailInfo> emailInfo = stringDeserialization(body);
				if (emailInfo.isPresent()) {
					emailService.sendEmailSync(emailInfo.get());
				} else {
					throw new Exception("Email info couldn't be parsed.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.log(Level.SEVERE, e.getMessage());
			}
		}
	}

	private static Optional<EmailInfo> stringDeserialization(String serializedString) {
		if (serializedString == null || serializedString.isEmpty() || serializedString.isBlank()) {
			return Optional.empty();
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			EmailInfo emailInfo = mapper.readValue(serializedString, EmailInfo.class);
			return Optional.of(emailInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
}
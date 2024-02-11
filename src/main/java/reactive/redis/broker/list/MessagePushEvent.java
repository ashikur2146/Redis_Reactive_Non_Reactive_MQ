package reactive.redis.broker.list;

import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class MessagePushEvent extends ApplicationEvent {

	private Message message;
	
	public MessagePushEvent(Message message) {
		super(message);
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}	
}
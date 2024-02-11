package reactive.redis.borker.pubsub.r;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

@Component
public class ReactiveRedisPublisher {

	private static final Logger LOGGER = Loggers.getLogger(ReactiveRedisPublisher.class);

	@Value("${spring.redis.channel.topic.R}")
    private String channel;

	private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

	public ReactiveRedisPublisher(ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
		super();
		this.reactiveRedisTemplate = reactiveRedisTemplate;
	}
	
	public Mono<Long> publishMessage(String message) {
		LOGGER.info("message published: " + message);
		return this.reactiveRedisTemplate.convertAndSend(this.channel, message);
	}
}
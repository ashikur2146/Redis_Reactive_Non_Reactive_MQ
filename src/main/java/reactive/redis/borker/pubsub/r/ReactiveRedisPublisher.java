package reactive.redis.borker.pubsub.r;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

@Component
public class ReactiveRedisPublisher<T, R> {

	private static final Logger LOGGER = Loggers.getLogger(ReactiveRedisPublisher.class);

	private ReactiveRedisTemplate<T, R> reactiveRedisTemplate;

	public ReactiveRedisPublisher(ReactiveRedisTemplate<T, R> reactiveRedisTemplate) {
		super();
		this.reactiveRedisTemplate = reactiveRedisTemplate;
	}

	public Mono<Long> publishMessage(T topic, R message) {
		Mono<Long> mono = Mono.empty();
		if (topic instanceof String) {
			mono = this.reactiveRedisTemplate.convertAndSend((String) topic, message);
		} else {
			mono = this.reactiveRedisTemplate.opsForList().leftPush(topic, message);
		}
		LOGGER.info("message published: " + message);
		return mono;
	}
}
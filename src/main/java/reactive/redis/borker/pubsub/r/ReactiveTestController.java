package reactive.redis.borker.pubsub.r;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/public/api/v1/reactive")
public class ReactiveTestController {
	
	@Value("${spring.redis.channel.topic.R}")
    private String channel;

	private ReactiveRedisPublisher<String, String> reactiveRedisPublisher;

	public ReactiveTestController(ReactiveRedisPublisher<String, String> reactiveRedisPublisher) {
		super();
		this.reactiveRedisPublisher = reactiveRedisPublisher;
	}

	@GetMapping("/publish")
	public Mono<String> publish() {
		 return reactiveRedisPublisher.publishMessage(this.channel, "REACTIVE MESSAGE PUBLISH")
	                .map(count -> "Message published successfully. Subscribers notified: " + count);
	}
}

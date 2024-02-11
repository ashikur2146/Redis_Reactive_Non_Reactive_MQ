package reactive.redis.borker.pubsub.r;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/public/api/v1/reactive")
public class ReactiveTestController {

	private ReactiveRedisPublisher reactiveRedisPublisher;

	public ReactiveTestController(ReactiveRedisPublisher reactiveRedisPublisher) {
		super();
		this.reactiveRedisPublisher = reactiveRedisPublisher;
	}

	@GetMapping("/publish")
	public Mono<String> publish() {
		 return reactiveRedisPublisher.publishMessage("REACTIVE MESSAGE PUBLISH")
	                .map(count -> "Message published successfully. Subscribers notified: " + count);
	}
}

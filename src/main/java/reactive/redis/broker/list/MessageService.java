package reactive.redis.broker.list;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class MessageService {
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    private final ApplicationEventPublisher eventPublisher;

    public MessageService(ReactiveRedisTemplate<String, String> reactiveRedisTemplate,
                          ApplicationEventPublisher eventPublisher) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.eventPublisher = eventPublisher;
    }

    public Mono<Long> pushMessage(String key, Message message) {
        return reactiveRedisTemplate.opsForList().leftPush(key, message.toString())
                .doOnNext(result -> eventPublisher.publishEvent(new MessagePushEvent(message)));
    }

    public Mono<String> popMessage(String key) {
        return reactiveRedisTemplate.opsForList().rightPop(key);
    }
}

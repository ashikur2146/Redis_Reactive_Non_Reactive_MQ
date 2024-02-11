package reactive.redis.broker.pubsub.x;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class RedisMessagePublisher<T, R> {

    @Value("${spring.redis.channel.topic.X}")
    private String channel;

    private final RedisTemplate<T, R> redisTemplate;

    public RedisMessagePublisher(RedisTemplate<T, R> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Long publishMessage(R message) {
        return redisTemplate.convertAndSend(this.channel, message);
    }

	public String getChannel() {
		return this.channel;
	}
}
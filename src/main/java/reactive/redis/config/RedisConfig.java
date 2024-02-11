package reactive.redis.config;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactive.redis.broker.pubsub.x.RedisMessagePublisher;
import reactive.redis.broker.pubsub.x.RedisMessageSubscriber;

@Configuration
@Lazy
public class RedisConfig<T, R> {
	private static final Logger LOGGER = Logger.getLogger(RedisConfig.class.getName());
	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Bean
	public RedisConnectionFactory connectionFactory() {
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(this.host, this.port);
		LOGGER.log(Level.INFO, "INFO: REDIS CONNECTION ESTABLISHED USING LETTUCE");
		return lettuceConnectionFactory;
	}

	@Bean
	public RedisMessagePublisher<T, R> redisMessagePublisher() {
		return new RedisMessagePublisher<>(this.redisTemplate());
	}

	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(RedisMessageSubscriber messageSubscriber) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.addMessageListener(messageSubscriber, new ChannelTopic(redisMessagePublisher().getChannel()));
		LOGGER.log(Level.INFO, "INFO: CONTAINER INITIALIZATION AND SUBSCRIPTION TO PUBSUB CHANNEL DONE.");
		return container;
	}

	@Bean
	public RedisTemplate<T, R> redisTemplate() {
		RedisTemplate<T, R> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory());
		ObjectMapper objectMapper = new ObjectMapper();
		Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper,
				Object.class);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
}
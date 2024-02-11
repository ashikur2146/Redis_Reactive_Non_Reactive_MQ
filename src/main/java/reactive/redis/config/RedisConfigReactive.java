package reactive.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import reactive.redis.borker.pubsub.r.ReactiveRedisPublisher;
import reactor.util.Logger;
import reactor.util.Loggers;

@Configuration
@Lazy
public class RedisConfigReactive {
	private static final Logger LOGGER = Loggers.getLogger(RedisConfigReactive.class);

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Bean
	@Primary
	public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(host, port);
		LOGGER.info("INFO: ** REACTIVE REDIS CONNECTION ESTABLISHED USING LETTUCE **");
		return lettuceConnectionFactory;
	}

	@Bean
	public ReactiveRedisPublisher reactiveRedisPublisher() {
		return new ReactiveRedisPublisher(this.reactiveRedisTemplate());
	}

	@Bean
	public ReactiveRedisMessageListenerContainer reactiveRedisMessageListenerContainer(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
		return new ReactiveRedisMessageListenerContainer(reactiveRedisConnectionFactory);
	}

	@Bean
	public ReactiveRedisTemplate<String, String> reactiveRedisTemplate() {
		StringRedisSerializer keySerializer = new StringRedisSerializer();
		Jackson2JsonRedisSerializer<String> valueSerializer = new Jackson2JsonRedisSerializer<>(String.class);
		RedisSerializationContext.RedisSerializationContextBuilder<String, String> builder = RedisSerializationContext
				.newSerializationContext(keySerializer);
		RedisSerializationContext<String, String> context = builder.value(valueSerializer).build();
		return new ReactiveRedisTemplate<String, String>(this.reactiveRedisConnectionFactory(), context);
	}
}
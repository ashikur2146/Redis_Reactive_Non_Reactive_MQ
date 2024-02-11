package reactive.redis.borker.pubsub.r;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.ReactiveSubscription.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.util.Logger;
import reactor.util.Loggers;

@Component
public class ReactiveRedisSubscriber {

    private static final Logger LOGGER = Loggers.getLogger(ReactiveRedisSubscriber.class);

    @Value("${spring.redis.channel.topic.R}")
    private String channel;

    private final ReactiveRedisMessageListenerContainer reactiveRedisMessageListenerContainer;

    public ReactiveRedisSubscriber(ReactiveRedisMessageListenerContainer reactiveRedisMessageListenerContainer) {
        this.reactiveRedisMessageListenerContainer = reactiveRedisMessageListenerContainer;
    }

    public void subscribeToChannel() {
        LOGGER.info("XX subscription channel: " + channel);
        reactiveRedisMessageListenerContainer.receive(ChannelTopic.of(channel))
                .subscribe(message -> {
                    LOGGER.info("** Received message: " + message.getMessage());
                });
    }
}

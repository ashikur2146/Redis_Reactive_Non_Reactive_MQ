package reactive.redis.broker.pubsub.x;

/**
 * Helps with identifying the content type propagated through a single message
 * queue.
 * <p>
 * PLAIN_TEXT: refers to text based contents
 * EMAIL: refers to contents sent over the SMTP protocol
 * BINARY: refers to byte contents
 * X: refers to custom type
 * </p>
 * <p>
 * Type should be defined before pushing to the queue, which then will be read
 * and processed by the regarding operation processor in the subscriber {@link RedisMessageSubscriber}.
 * </p>
 */
enum MESSAGE_TYPE {
	PLAIN_TEXT,
	EMAIL,
	BINARY,
	X
}
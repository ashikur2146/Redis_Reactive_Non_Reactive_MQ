package reactive.redis.broker.list;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MessageEventListener {
    private final MessageService messageService;

    public MessageEventListener(MessageService messageService) {
        this.messageService = messageService;
    }

    @EventListener
    public void handleMessagePushedEvent(MessagePushEvent event) {
        messageService.popMessage("messages").subscribe(
                poppedMessage -> {
                    System.out.println("Popped message: " + poppedMessage);
                },
                error -> {
                    System.err.println("Error while popping message: " + error.getMessage());
                }
        );
    }
}
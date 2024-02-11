package reactive.email;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/api/v2/email")
public class EmailController {

    private final EmailService<EmailInfo, String> emailService;

    public EmailController(EmailService<EmailInfo, String> emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send")
    public ResponseEntity<String> sendEmail() throws Exception {
    	EmailInfo emailInfo = new EmailInfo("test@test.com", "*** IGNORE THIS EMAIL ***", "*** IGNORE THIS EMAIL ***");
        return ResponseEntity.ok(emailService.sendEmail(emailInfo));
    }
}
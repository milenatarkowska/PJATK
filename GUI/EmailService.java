import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class EmailService {

    public EmailService(String to, String subject, String body) {

        //MailerBuilder.withSMTPServer("", , "", "").buildMailer().sendMail(EmailBuilder.startingBlank().from("").to(to, to).withSubject(subject).withPlainText(body).buildEmail());
    }

}


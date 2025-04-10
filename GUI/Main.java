import org.apache.log4j.BasicConfigurator;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;

import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        SwingUtilities.invokeLater(AppHome::new);

        //Mailer mailer = MailerBuilder.withSMTPServer("", , "", "").buildMailer();

        //mailer.testConnection();

    }

}

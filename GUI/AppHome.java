import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class AppHome extends JFrame {

    private DefaultListModel<String> emailHistoryModel;
    private JList<String> emailHistoryList;
    private JTextArea emailDataArea;
    private HashMap<String, String> userData;
    private JTextField emailSubjectField;
    private JTextField emailRecipientField;

    public AppHome() throws HeadlessException{

        userData = new HashMap<>();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        setLayout(new BorderLayout());

        setPreferredSize(new Dimension(600,400));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        // tytul aplikacji
        setTitle("PJATK MAIL APP");

        // panel gorny na przyciski
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        // przycisk utworz mail
        JButton createMailButton = new JButton("Create e-mail message");
        createMailButton.addActionListener(e -> {
            CreateEmail createEmail = new CreateEmail(emailHistoryModel, userData);
            createEmail.setVisible(true);
        });
        topPanel.add(createMailButton);

        // przycisk dodaj kontakt
        JButton addContactButton = new JButton("Add to contact list");
        addContactButton.addActionListener(e -> {
            AddContact addContact = new AddContact(userData);
            addContact.setVisible(true);
        });
        topPanel.add(addContactButton);

        add(topPanel, BorderLayout.NORTH);

        //lewy panel na historie maili
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        JLabel emailHistoryLabel = new JLabel("Mail history");
        leftPanel.add(emailHistoryLabel, BorderLayout.NORTH);

        emailHistoryModel = new DefaultListModel<>();
        emailHistoryList = new JList<>(emailHistoryModel);
        emailHistoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        emailHistoryList.addListSelectionListener(e -> showEmailDetails());
        JScrollPane emailHistoryScrollPane = new JScrollPane(emailHistoryList);
        leftPanel.add(emailHistoryScrollPane, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);

        // panel centralny - pokazuje detale maila
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        JPanel emailDetailsPanel = new JPanel(new GridLayout(2,1));
        emailDetailsPanel.setBackground(Color.LIGHT_GRAY);

        emailRecipientField = new JTextField();
        emailRecipientField.setEditable(false);
        emailDetailsPanel.add(emailRecipientField);

        emailSubjectField = new JTextField();
        emailSubjectField.setEditable(false);
        emailDetailsPanel.add(emailSubjectField);

        centerPanel.add(emailDetailsPanel, BorderLayout.NORTH);

        emailDataArea = new JTextArea();
        emailDataArea.setEditable(false);
        JScrollPane emailDataScrollPane = new JScrollPane(emailDataArea);
        centerPanel.add(emailDataScrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);


    }
    private void showEmailDetails(){
        String selectedEmail = emailHistoryList.getSelectedValue();

        if(selectedEmail != null){
            String[] emailParts = selectedEmail.split("\n",3);
            String recipient = emailParts[0].substring(4).trim();
            String subject = emailParts[1].substring(8).trim();
            String content = emailParts[2].substring(9).trim();

            emailRecipientField.setText("To: " + recipient);
            emailSubjectField.setText("Subject: " + subject);
            emailDataArea.setText("Message: \n" + content);
        } else {
            emailRecipientField.setText("");
            emailSubjectField.setText("");
            emailDataArea.setText("");
        }
    }
}

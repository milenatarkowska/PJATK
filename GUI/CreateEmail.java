import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CreateEmail extends JDialog {

    private JList<Object> contactsList;
    private DefaultListModel<Object> listModel;
    private DefaultListModel<String> emailHistoryModel;
    private JTextField subjectField;
    private JTextArea messageArea;
    private JTextField mailField;
    private HashMap<String, String> userData;

    public CreateEmail(DefaultListModel<String> emailHistoryModel, HashMap<String, String> userData){
        setTitle("Create e-mail message");
        setPreferredSize(new Dimension(700,300));
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setModal(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        this.emailHistoryModel = emailHistoryModel;
        this.userData = userData;

        // panel glowny
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // panel do wpisywania kontaktu i tematu wiadomosci
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        upperPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.add(upperPanel, BorderLayout.PAGE_START);

        // kontakt (wybierz adres email)
        JLabel contactLabel = new JLabel("To: ");
        setContraints(constraints,0,0,0.1);
        upperPanel.add(contactLabel, constraints);

        // pole na adres email
        mailField = new JTextField();
        setContraints(constraints,1,0,0.9);
        upperPanel.add(mailField, constraints);

        // przycisk wybierania kontaktu
        // otwiera okienko z lista kontaktow do wyboru, pobiera adres email w pole tekstowe

        JButton contactsButton = new JButton("Contacts");
        setContraints(constraints,2,0,1.0);
        constraints.gridwidth = 2;
        upperPanel.add(contactsButton, constraints);

        // action listener dla kontaktow
        contactsButton.addActionListener(e -> {
            ContactList contactList = new ContactList(CreateEmail.this, userData);
            contactList.setVisible(true);
            String selectedContact = contactList.getSelectedContact();
            if (selectedContact != null) {
                mailField.setText(selectedContact);
            }
        });

        // label tematu
        JLabel subjestLabel = new JLabel("Subject: ");
        setContraints(constraints,0,1,0.1);
        upperPanel.add(subjestLabel, constraints);

        // pole tematu
        subjectField = new JTextField();
        subjectField.setEditable(true);
        setContraints(constraints,1,1,0.9);
        upperPanel.add(subjectField, constraints);

        // pole na wiadomosc
        messageArea = new JTextArea();
        mainPanel.add(messageArea, BorderLayout.CENTER);

        // panel na przyciski
        JPanel lowerPanel = new JPanel(new FlowLayout());
        mainPanel.add(lowerPanel, BorderLayout.PAGE_END);

        // przycisk wyslij
        JButton sendButton = new JButton("Send");
        lowerPanel.add(sendButton);

        // action listener dla wysylania
        sendButton.addActionListener(e -> sendEmail());

        // przycisk odrzuc
        JButton cancelButton = new JButton("Cancel");
        lowerPanel.add(cancelButton);

        // action listener dla anulacji
        cancelButton.addActionListener(e -> dispose());
    }

    public void setContraints(GridBagConstraints contraints, int gridx, int gridy, double weightx){
        contraints.gridx = gridx;
        contraints.gridy = gridy;
        contraints.weightx = weightx;
    }

    private void sendEmail(){
        String email = mailField.getText().trim();
        String subject = subjectField.getText().trim();
        String message = messageArea.getText().trim();

        // walidacja pol
        if(email.isEmpty() || subject.isEmpty() || message.isEmpty()){
            JOptionPane.showMessageDialog(this, "Fill in all fields to continue", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!email.contains("@")){
            JOptionPane.showMessageDialog(this, "Invalid e-mail address", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // zapisuje email do historii
        String emailContent = String.format("To: %s \n" +
                "Subject: %s \n Message: \n %s", email, subject, message);
        emailHistoryModel.addElement(emailContent);

        new EmailService(mailField.getText(), subjectField.getText(), messageArea.getText());

        // czysci pola wpisywania
        mailField.setText("");
        subjectField.setText("");
        messageArea.setText("");

        JOptionPane.showMessageDialog(this, "E-mail sent correctly!", "Success!", JOptionPane.INFORMATION_MESSAGE);
    }


}

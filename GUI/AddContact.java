import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class AddContact extends JDialog {

    private DefaultListModel<String> listModel;
    private JList<String> contactsList;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField emailAddressField;
    private Map<String, String> userData;

    public AddContact(HashMap<String, String> userData){

        setTitle("Add contact");
        setPreferredSize(new Dimension(600,300));
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setModal(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);


        // inicjalizacja mapy z kontaktami
        this.userData = userData;

        // dodawanie danych
        // panel

        JPanel addingPanel = new JPanel();
        addingPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // imie - pole tekstowe i label
        JLabel nameLabel = new JLabel("Name: ");
        setContraints(constraints,0,0,0.1);
        addingPanel.add(nameLabel, constraints);

        nameField = new JTextField();
        setContraints(constraints,1,0,0.9);
        addingPanel.add(nameField, constraints);

        // nazwisko - pole tekstowe i label
        JLabel surnameLabel = new JLabel("Surname: ");
        setContraints(constraints,0,1,0.1);
        addingPanel.add(surnameLabel, constraints);

        surnameField = new JTextField();
        setContraints(constraints,1,1,0.9);
        addingPanel.add(surnameField, constraints);

        // adres email - pole tekstowe i label
        JLabel emailLabel = new JLabel("E-mail address: ");
        setContraints(constraints,0,2,0.1);
        addingPanel.add(emailLabel, constraints);

        emailAddressField = new JTextField();
        setContraints(constraints,1,2,0.9);
        addingPanel.add(emailAddressField, constraints);

        // przycisk dodawania kontaktu
        JButton addButton = new JButton("Add contact");
        setContraints(constraints,0,3,1.0);
        constraints.gridwidth = 2;
        addingPanel.add(addButton, constraints);

        // action listener dla dodawania
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });

        // przycisk anulacji
        JButton cancelButton = new JButton("Cancel");
        setContraints(constraints,1,3,1.0);
        constraints.gridwidth = 2;
        addingPanel.add(cancelButton, constraints);

        // action listener dla anulacji
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // lista
       //TODO:  JLabel listLabel = new JLabel("Currently added contacts"); USTAWIENIE

        listModel = new DefaultListModel<>();
        contactsList = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(contactsList);


        // layout panelu
        setLayout(new BorderLayout());
        add(listScrollPane, BorderLayout.EAST);
        add(addingPanel, BorderLayout.CENTER);

    }

    public void setContraints(GridBagConstraints contraints, int gridx, int gridy, double weightx){
        contraints.gridx = gridx;
        contraints.gridy = gridy;
        contraints.weightx = weightx;
    }

    public void addContact(){
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String email = emailAddressField.getText().trim();

        if(name.isEmpty() || surname.isEmpty() || email.isEmpty()){
            JOptionPane.showMessageDialog(this, "Fill in all fields to continue","Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!email.contains("@")){
            JOptionPane.showMessageDialog(this, "Invalid e-mail address", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(userData.containsKey(email)){
            JOptionPane.showMessageDialog(this, "Duplicated contact", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // dodaje kontakt do listy
        String contactInfo = String.format("%s %s - %s",name, surname, email);
        listModel.addElement(contactInfo);
        userData.put(email, contactInfo);

        //czysci pola do wpisywania danych
        nameField.setText("");
        surnameField.setText("");
        emailAddressField.setText("");
    }

}

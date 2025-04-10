import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ContactList extends JDialog {
    private JList<String> contactList;
    private DefaultListModel<String> contactListModel;
    private String selectedContact;
    private Map<String, String> userData;

    public ContactList(CreateEmail parent, HashMap<String, String> userData) {

        super(parent, "Select Contact", true);
        setPreferredSize(new Dimension(400,500));
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        this.userData = userData;

        contactListModel = new DefaultListModel<>();
        contactList = new JList<>(contactListModel);
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactList.addListSelectionListener(e -> {
            selectedContact = contactList.getSelectedValue();
        });

        JScrollPane scrollPane = new JScrollPane(contactList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel lowerPanel = new JPanel(new FlowLayout());
        add(lowerPanel, BorderLayout.PAGE_END);

        JButton selectButton = new JButton("Select");
        lowerPanel.add(selectButton);

        selectButton.addActionListener(e -> dispose());

        JButton cancelButton = new JButton("Cancel");
        lowerPanel.add(cancelButton);

        cancelButton.addActionListener(e -> {
            selectedContact = null;
            dispose();
        });

        loadContacts();
    }

    public String getSelectedContact() {
        return selectedContact;
    }

    private void loadContacts(){
        contactListModel.clear();
        for(String contact : userData.keySet()){
            contactListModel.addElement(contact);
        }
    }

}


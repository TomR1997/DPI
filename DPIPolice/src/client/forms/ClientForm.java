package client.forms;

/**
 *
 * @author Tomt
 */
import client.models.ClientReply;
import client.models.ClientRequest;
import gateway.ClientGateway;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import message.RequestReply;
import observer.Observer;

public class ClientForm extends JFrame implements Observer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField locationReply;
    private JTextField licenceReply;
    private JTextField clientNameReply;
    private DefaultListModel<RequestReply<ClientRequest, ClientReply>> listModel = new DefaultListModel<>();
    private JList<RequestReply<ClientRequest, ClientReply>> requestReplyList;

    private ClientGateway gateway = new ClientGateway("clientRequest", "clientReply");

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ClientForm frame = new ClientForm();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public ClientForm() {
        setTitle("Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{46, 31, 86, 30, 89, 0};
        gbl_contentPane.rowHeights = new int[]{233, 23, 0};
        gbl_contentPane.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.gridwidth = 5;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        contentPane.add(scrollPane, gbc_scrollPane);

        //scrollPane.setViewportView(list);
        JLabel locationLbl = new JLabel("location");
        GridBagConstraints gbc_locationbLbl = new GridBagConstraints();
        gbc_locationbLbl.anchor = GridBagConstraints.EAST;
        gbc_locationbLbl.insets = new Insets(0, 0, 0, 5);
        gbc_locationbLbl.gridx = 0;
        gbc_locationbLbl.gridy = 1;
        contentPane.add(locationLbl, gbc_locationbLbl);

        locationReply = new JTextField();
        GridBagConstraints gbc_locationReply = new GridBagConstraints();
        gbc_locationReply.gridwidth = 2;
        gbc_locationReply.insets = new Insets(0, 0, 0, 5);
        gbc_locationReply.fill = GridBagConstraints.HORIZONTAL;
        gbc_locationReply.gridx = 1;
        gbc_locationReply.gridy = 1;
        contentPane.add(locationReply, gbc_locationReply);
        locationReply.setColumns(10);

        JLabel licenceLbl = new JLabel("licenceplate");
        GridBagConstraints gbc_licenceLbl = new GridBagConstraints();
        gbc_licenceLbl.anchor = GridBagConstraints.EAST;
        gbc_licenceLbl.insets = new Insets(0, 0, 0, 5);
        gbc_licenceLbl.gridx = 0;
        gbc_licenceLbl.gridy = 4;
        contentPane.add(licenceLbl, gbc_licenceLbl);

        licenceReply = new JTextField();
        GridBagConstraints gbc_licenceReply = new GridBagConstraints();
        gbc_licenceReply.gridwidth = 2;
        gbc_licenceReply.insets = new Insets(0, 0, 0, 5);
        gbc_licenceReply.fill = GridBagConstraints.HORIZONTAL;
        gbc_licenceReply.gridx = 1;
        gbc_licenceReply.gridy = 4;
        contentPane.add(licenceReply, gbc_licenceReply);
        licenceReply.setColumns(10);

        JLabel clientNameLbl = new JLabel("name");
        GridBagConstraints gbc_clientNameLbl = new GridBagConstraints();
        gbc_clientNameLbl.anchor = GridBagConstraints.EAST;
        gbc_clientNameLbl.insets = new Insets(0, 0, 0, 5);
        gbc_clientNameLbl.gridx = 0;
        gbc_clientNameLbl.gridy = 7;
        contentPane.add(clientNameLbl, gbc_clientNameLbl);

        clientNameReply = new JTextField();
        GridBagConstraints gbc_clientNameReply = new GridBagConstraints();
        gbc_clientNameReply.gridwidth = 2;
        gbc_clientNameReply.insets = new Insets(0, 0, 0, 5);
        gbc_clientNameReply.fill = GridBagConstraints.HORIZONTAL;
        gbc_clientNameReply.gridx = 1;
        gbc_clientNameReply.gridy = 7;
        contentPane.add(clientNameReply, gbc_clientNameReply);
        licenceReply.setColumns(10);

        JButton sendRequestBtn = new JButton("send request");
        sendRequestBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String licenceplate = licenceReply.getText();
                String location = locationReply.getText();
                String name = clientNameReply.getText();
                ClientRequest request = new ClientRequest(location, licenceplate, name);
                listModel.addElement(new RequestReply<>(request, null));
                gateway.sendRequest(request);
            }
        });

        GridBagConstraints gbc_sendRequest = new GridBagConstraints();
        gbc_sendRequest.anchor = GridBagConstraints.NORTHWEST;
        gbc_sendRequest.gridx = 4;
        gbc_sendRequest.gridy = 1;
        contentPane.add(sendRequestBtn, gbc_sendRequest);

        requestReplyList = new JList<>(listModel);
        scrollPane.setViewportView(requestReplyList);

        gateway.addObserver(this);
    }

    private RequestReply<ClientRequest, ClientReply> getRequestReply(ClientRequest request) {

        for (int i = 0; i < listModel.getSize(); i++) {
            RequestReply<ClientRequest, ClientReply> rr = listModel.get(i);
            if (rr.getRequest() == request) {
                return rr;
            }
        }

        return null;
    }

    @Override
    public void update(Object... args) {
        ClientRequest request = (ClientRequest) args[0];
        ClientReply reply = (ClientReply) args[1];
        RequestReply<ClientRequest, ClientReply> rr = getRequestReply(request);
        rr.setReply(reply);
        requestReplyList.repaint();
    }

}

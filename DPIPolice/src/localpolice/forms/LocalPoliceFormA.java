package localpolice.forms;

import gateway.LocalPoliceGateway;
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
import localpolice.models.LocalPoliceReply;
import localpolice.models.LocalPoliceRequest;
import message.RequestReply;

import observer.Observer;

public class LocalPoliceFormA extends JFrame implements Observer {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField foundReply;
    private JTextField locationReply;
    private DefaultListModel<RequestReply<LocalPoliceRequest, LocalPoliceReply>> listModel = new DefaultListModel<>();
    private final String localPoliceName = "localPoliceRequestA";
    private LocalPoliceGateway gateway = new LocalPoliceGateway("Eindhoven", "LocalPoliceReplyA", localPoliceName);

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LocalPoliceFormA form = new LocalPoliceFormA();
                    form.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LocalPoliceFormA() {
        setTitle("Local Police A");
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

        JList<RequestReply<LocalPoliceRequest, LocalPoliceReply>> list = new JList<>(listModel);
        scrollPane.setViewportView(list);

        JLabel lblFound = new JLabel("found");
        GridBagConstraints gbc_lblFound = new GridBagConstraints();
        gbc_lblFound.anchor = GridBagConstraints.EAST;
        gbc_lblFound.insets = new Insets(0, 0, 0, 5);
        gbc_lblFound.gridx = 0;
        gbc_lblFound.gridy = 1;
        contentPane.add(lblFound, gbc_lblFound);

        foundReply = new JTextField();
        GridBagConstraints gbc_foundReply = new GridBagConstraints();
        gbc_foundReply.gridwidth = 2;
        gbc_foundReply.insets = new Insets(0, 0, 0, 5);
        gbc_foundReply.fill = GridBagConstraints.HORIZONTAL;
        gbc_foundReply.gridx = 1;
        gbc_foundReply.gridy = 1;
        contentPane.add(foundReply, gbc_foundReply);
        foundReply.setColumns(10);

        JLabel lblLocation = new JLabel("location");
        GridBagConstraints gbc_lblLocation = new GridBagConstraints();
        gbc_lblLocation.anchor = GridBagConstraints.EAST;
        gbc_lblLocation.insets = new Insets(0, 0, 0, 5);
        gbc_lblLocation.gridx = 0;
        gbc_lblLocation.gridy = 4;
        contentPane.add(lblLocation, gbc_lblLocation);

        locationReply = new JTextField();
        GridBagConstraints gbc_locationReply = new GridBagConstraints();
        gbc_locationReply.gridwidth = 2;
        gbc_locationReply.insets = new Insets(0, 0, 0, 5);
        gbc_locationReply.fill = GridBagConstraints.HORIZONTAL;
        gbc_locationReply.gridx = 1;
        gbc_locationReply.gridy = 4;
        contentPane.add(locationReply, gbc_locationReply);
        locationReply.setColumns(10);

        JButton btnSendReply = new JButton("send reply");
        btnSendReply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RequestReply<LocalPoliceRequest, LocalPoliceReply> rr = list.getSelectedValue();
                boolean found = Boolean.parseBoolean(foundReply.getText());
                String location = locationReply.getText();
                LocalPoliceReply reply = new LocalPoliceReply(found, location, localPoliceName);
                if (rr != null && reply != null) {
                    rr.setReply(reply);
                    list.repaint();
                    gateway.sendReply(rr.getRequest(), rr.getReply());
                }
            }
        });
        GridBagConstraints gbc_btnSendReply = new GridBagConstraints();
        gbc_btnSendReply.anchor = GridBagConstraints.NORTHWEST;
        gbc_btnSendReply.gridx = 4;
        gbc_btnSendReply.gridy = 1;
        contentPane.add(btnSendReply, gbc_btnSendReply);

        gateway.addObserver(this);
    }

    public void add(LocalPoliceRequest request) {
        RequestReply<LocalPoliceRequest, LocalPoliceReply> rr = new RequestReply<>(request, null);
        listModel.addElement(rr);
    }

    @Override
    public void update(Object... args) {
        LocalPoliceRequest request = (LocalPoliceRequest) args[0];
        add(request);
    }

}

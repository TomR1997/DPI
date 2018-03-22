package abnamro.forms;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import abnamro.models.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import loanbroker.forms.LoanBrokerFrame;
import messaging.IMessageRequest;
import messaging.MessageRequest;
import requestreply.RequestReply;

public class JMSBankFrame extends JFrame implements MessageListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfReply;
    private DefaultListModel<RequestReply<BankInterestRequest, BankInterestReply>> listModel = new DefaultListModel<RequestReply<BankInterestRequest, BankInterestReply>>();
    private Map<BankInterestRequest, String> bankInterestRequests = new HashMap<>();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                JMSBankFrame frame = new JMSBankFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public JMSBankFrame() {
        setTitle("JMS Bank - ABN AMRO");
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

        JList<RequestReply<BankInterestRequest, BankInterestReply>> list = new JList<>(listModel);
        scrollPane.setViewportView(list);

        JLabel lblNewLabel = new JLabel("type reply");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 1;
        contentPane.add(lblNewLabel, gbc_lblNewLabel);

        tfReply = new JTextField();
        GridBagConstraints gbc_tfReply = new GridBagConstraints();
        gbc_tfReply.gridwidth = 2;
        gbc_tfReply.insets = new Insets(0, 0, 0, 5);
        gbc_tfReply.fill = GridBagConstraints.HORIZONTAL;
        gbc_tfReply.gridx = 1;
        gbc_tfReply.gridy = 1;
        contentPane.add(tfReply, gbc_tfReply);
        tfReply.setColumns(10);

        new MessageRequest().receive(BankInterestRequest.class, new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                try {
                    if (msg instanceof ObjectMessage) {
                        IMessageRequest msgObj = (IMessageRequest) ((ObjectMessage) msg).getObject();
                        if (msgObj instanceof BankInterestRequest) {
                            //Receive bank interest request
                            BankInterestRequest bankInterestRequest = (BankInterestRequest) msgObj;
                            add(bankInterestRequest);
                            bankInterestRequests.put(bankInterestRequest, msg.getJMSCorrelationID());
                            System.out.println("Receiving: " + bankInterestRequest);
                        }
                    }
                } catch (JMSException ex) {
                    Logger.getLogger(JMSBankFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );

        JButton btnSendReply = new JButton("send reply");
        btnSendReply.addActionListener((ActionEvent e) -> {
            RequestReply<BankInterestRequest, BankInterestReply> rr = list.getSelectedValue();
            double interest = Double.parseDouble((tfReply.getText()));
            BankInterestReply reply = new BankInterestReply(interest, "ABN AMRO");
            if (rr != null && reply != null) {
                rr.setReply(reply);
                list.repaint();
                new MessageRequest().send(reply, bankInterestRequests.get(rr.getRequest()));
                System.out.println("Sending: " + reply);
            }
        });
        GridBagConstraints gbc_btnSendReply = new GridBagConstraints();
        gbc_btnSendReply.anchor = GridBagConstraints.NORTHWEST;
        gbc_btnSendReply.gridx = 4;
        gbc_btnSendReply.gridy = 1;
        contentPane.add(btnSendReply, gbc_btnSendReply);
    }

    @Override
    public void onMessage(Message msg) {
        TextMessage textMessage = (TextMessage) msg;
        try {
            System.out.println("received: " + textMessage.getText());
            listModel.addElement(new RequestReply<>(createBankInterestRequest(textMessage), null));
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }

    private BankInterestRequest createBankInterestRequest(TextMessage msg) {
        String[] data = null;
        try {
            data = msg.getText().split(";");
        } catch (JMSException ex) {
            Logger.getLogger(LoanBrokerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        BankInterestRequest request = null;
        if (data != null) {
            request = new BankInterestRequest(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
        }
        return request;
    }

    public void add(BankInterestRequest bankInterestRequest) {
        RequestReply<BankInterestRequest, BankInterestReply> rr = new RequestReply<>(bankInterestRequest, null);
        listModel.addElement(rr);
    }

}

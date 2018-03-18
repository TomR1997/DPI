package loanbroker.forms;

import abnamro.models.BankInterestReply;
import abnamro.models.BankInterestRequest;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import loanbroker.models.LoanRequest;
import messaging.ConsumerMessengerListener;
import messaging.ReceiveRequest;

public class LoanBrokerFrame extends JFrame implements MessageListener {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DefaultListModel<JListLine> listModel = new DefaultListModel<>();
    private JList<JListLine> list;
    private static ReceiveRequest receiveRequest = new ReceiveRequest();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoanBrokerFrame frame = new LoanBrokerFrame();
                frame.setVisible(true);
                receiveRequest.receiveMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public LoanBrokerFrame() {
        setTitle("Loan Broker");
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
        gbc_scrollPane.gridwidth = 7;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        contentPane.add(scrollPane, gbc_scrollPane);

        list = new JList<>(listModel);
        scrollPane.setViewportView(list);
    }

    private JListLine getRequestReply(LoanRequest request) {
        for (int i = 0; i < listModel.getSize(); i++) {
            JListLine rr = listModel.get(i);
            if (rr.getLoanRequest() == request) {
                return rr;
            }
        }

        return null;
    }

    public void add(LoanRequest loanRequest) {
        listModel.addElement(new JListLine(loanRequest));
    }

    public void add(LoanRequest loanRequest, BankInterestRequest bankRequest) {
        JListLine rr = getRequestReply(loanRequest);
        if (rr != null && bankRequest != null) {
            rr.setBankRequest(bankRequest);
            list.repaint();
        }
    }

    public void add(LoanRequest loanRequest, BankInterestReply bankReply) {
        JListLine rr = getRequestReply(loanRequest);
        if (rr != null && bankReply != null) {
            rr.setBankReply(bankReply);
            list.repaint();
        }
    }

    @Override
    public void onMessage(Message msg) {
        TextMessage textMessage = (TextMessage) msg;
        try{
            System.out.println("received: " + textMessage.getText());
            createBankInterestRequest((TextMessage)msg);
        } catch (JMSException ex){
            ex.printStackTrace();
        }
    }
    
    private BankInterestRequest createBankInterestRequest(TextMessage msg){
        String[] data = null;
        try {
            data = msg.getText().split(";");
        } catch (JMSException ex) {
            Logger.getLogger(LoanBrokerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        BankInterestRequest request = null;
        if (data != null){
            request = new BankInterestRequest(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
        }
        
        return request;
    }
}

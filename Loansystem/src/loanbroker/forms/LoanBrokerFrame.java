package loanbroker.forms;

import abnamro.models.BankInterestReply;
import abnamro.models.BankInterestRequest;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import loanbroker.models.LoanReply;

import loanbroker.models.LoanRequest;
import messaging.IMessageRequest;
import messaging.MessageRequest;

public class LoanBrokerFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DefaultListModel<JListLine> listModel = new DefaultListModel<>();
    private JList<JListLine> list;
    private Map<String, LoanRequest> loanRequests = new HashMap<>();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoanBrokerFrame frame = new LoanBrokerFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public LoanBrokerFrame() {
        new MessageRequest().receive(LoanRequest.class, new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                try {
                    if (msg instanceof ObjectMessage) {
                        IMessageRequest msgRequest = (IMessageRequest) ((ObjectMessage) msg).getObject();
                        if (msgRequest instanceof LoanRequest) {
                            //Receive loan request
                            LoanRequest loanRequest = (LoanRequest) msgRequest;
                            loanRequests.put(msg.getJMSMessageID(), loanRequest);
                            add(loanRequest);
                            System.out.println("Receiving: " + loanRequest);
                            //Send bank interest request
                            BankInterestRequest bankInterestRequest = new BankInterestRequest(loanRequest.getTime(), loanRequest.getAmount());
                            add(loanRequest, bankInterestRequest);
                            new MessageRequest().send(bankInterestRequest, msg.getJMSMessageID());
                            System.out.println("Sending: " + bankInterestRequest);
                        }
                    }
                } catch (JMSException ex) {
                    Logger.getLogger(LoanBrokerFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        new MessageRequest().receive(BankInterestReply.class, new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                try {
                    if (msg instanceof ObjectMessage) {
                        IMessageRequest msgObj = (IMessageRequest) ((ObjectMessage) msg).getObject();
                        if (msgObj instanceof BankInterestReply) {
                            //Receive bank interest reply
                            BankInterestReply bankInterestReply = (BankInterestReply) msgObj;
                            add(loanRequests.get(msg.getJMSCorrelationID()), bankInterestReply);
                            list.repaint();
                            System.out.println("Receiving: " + bankInterestReply);
                            //Send loan reply
                            LoanReply loanReply = new LoanReply(bankInterestReply.getInterest(), bankInterestReply.getQuoteId());
                            new MessageRequest().send(loanReply, msg.getJMSCorrelationID());
                            System.out.println("Sending: " + loanReply);
                        }
                    }
                } catch (JMSException ex) {
                    Logger.getLogger(LoanBrokerFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
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
}

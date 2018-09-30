package centralpolice.forms;

import client.models.ClientRequest;
import gateway.RecipientManager;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import livepolice.models.LivePoliceReply;
import localpolice.models.LocalPoliceReply;
import localpolice.models.LocalPoliceRequest;
import observer.Observer;
import message.JListLine;

public class CentralPoliceForm extends JFrame implements Observer {

    private DefaultListModel<JListLine> listModel = new DefaultListModel<>();
    private JList<JListLine> list;
    private JPanel contentPane;
    private RecipientManager manager = new RecipientManager();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CentralPoliceForm frame = new CentralPoliceForm();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CentralPoliceForm() {
        setTitle("Central police");
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

        manager.addObserver(this);
    }

    private JListLine getRequestReply(ClientRequest request) {
        for (int i = 0; i < listModel.getSize(); i++) {
            JListLine rr = listModel.get(i);
            if (rr.getClientRequest() == request) {
                return rr;
            }
        }

        return null;
    }   

    public void add(ClientRequest clientRequest) {
        listModel.addElement(new JListLine(clientRequest));
    }

    public void add(ClientRequest clientRequest, LocalPoliceRequest localPoliceRequest) {
        JListLine rr = getRequestReply(clientRequest);
        if (rr != null && localPoliceRequest != null) {
            rr.setLocalPoliceRequest(localPoliceRequest);
            list.repaint();
        }
    }

    public void add(ClientRequest clientRequest, LocalPoliceReply localPoliceReply) {
        JListLine rr = getRequestReply(clientRequest);
        if (rr != null && localPoliceReply != null) {
            rr.setLocalPoliceReply(localPoliceReply);
            list.repaint();
        }
    }
    
    public void add(ClientRequest clientRequest, LivePoliceReply livePoliceReply) {
        JListLine rr = getRequestReply(clientRequest);
        if (rr != null && livePoliceReply != null) {
            rr.setLivePoliceReply(livePoliceReply);
            list.repaint();
        }
    }

    @Override
    public void update(Object... args) {
        if (args.length == 1) {
            ClientRequest request = (ClientRequest) args[0];
            add(request);
        }
        else if (args.length == 2) {
            ClientRequest request = (ClientRequest) args[0];
            if (args[1] instanceof LocalPoliceReply){
                LocalPoliceReply reply = (LocalPoliceReply) args[1];
                add(request, reply);
            }else if (args[1] instanceof LivePoliceReply){
                LivePoliceReply reply = (LivePoliceReply) args[1];
                add(request, reply);
            }
        }
    }
}

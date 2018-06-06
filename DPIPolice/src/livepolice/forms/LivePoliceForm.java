package livepolice.forms;

import gateway.LivePoliceGateway;
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
import livepolice.models.LivePoliceRequest;
import observer.Observer;
import message.JListLine;
import message.RequestReply;

public class LivePoliceForm extends JFrame implements Observer {

    private DefaultListModel<RequestReply<LivePoliceRequest, LivePoliceReply>> listModel = new DefaultListModel<>();
    private JList<JListLine> list;
    private JPanel contentPane;
    private LivePoliceGateway gateway = new LivePoliceGateway("livePoliceRequest", "livePoliceReply");

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LivePoliceForm frame = new LivePoliceForm();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LivePoliceForm() {
        setTitle("Live police");
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
        
        JList<RequestReply<LivePoliceRequest, LivePoliceReply>> list = new JList<>(listModel);
        scrollPane.setViewportView(list);

        //list = new JList<>(listModel);
        //scrollPane.setViewportView(list);

    }

    @Override
    public void update(Object... args) {
        LivePoliceRequest request = (LivePoliceRequest) args[0];
        add(request);
    }
    
    public void add (LivePoliceRequest request){
        RequestReply<LivePoliceRequest, LivePoliceReply> rr  = new RequestReply<>(request, null);
        listModel.addElement(rr);
    }
}

package client.forms;

/**
 *
 * @author Tomt
 */

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ClientForm extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField locationReply;
    private JTextField licenceReply;

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

        JLabel locationLbl = new JLabel("enter location");
        GridBagConstraints gbc_locatiobLbl = new GridBagConstraints();
        gbc_locatiobLbl.anchor = GridBagConstraints.EAST;
        gbc_locatiobLbl.insets = new Insets(0, 0, 0, 5);
        gbc_locatiobLbl.gridx = 0;
        gbc_locatiobLbl.gridy = 1;
        contentPane.add(locationLbl, gbc_locatiobLbl);

        locationReply = new JTextField();
        GridBagConstraints gbc_locationLbl = new GridBagConstraints();
        gbc_locationLbl.gridwidth = 2;
        gbc_locationLbl.insets = new Insets(0, 0, 0, 5);
        gbc_locationLbl.fill = GridBagConstraints.HORIZONTAL;
        gbc_locationLbl.gridx = 1;
        gbc_locationLbl.gridy = 4;
        contentPane.add(locationReply, gbc_locationLbl);
        locationReply.setColumns(10);
        
        JLabel licenceLbl = new JLabel("enter licenceplate");
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
        gbc_licenceReply.gridy = 1;
        contentPane.add(licenceReply, gbc_licenceReply);
        licenceReply.setColumns(10);

        JButton sendRequestBtn = new JButton("send request");
        sendRequestBtn.addActionListener((ActionEvent e) -> {
        });
        
        GridBagConstraints gbc_sendRequest = new GridBagConstraints();
        gbc_sendRequest.anchor = GridBagConstraints.NORTHWEST;
        gbc_sendRequest.gridx = 4;
        gbc_sendRequest.gridy = 1;
        contentPane.add(sendRequestBtn, gbc_sendRequest);
    }

}


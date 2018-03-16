/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author Tomt
 */
public class ConsumerMessengerListener implements MessageListener {

    public ConsumerMessengerListener() {
    }
    
    
    @Override
    public void onMessage(Message msg) {
        TextMessage textMessage = (TextMessage) msg;
        try{
            System.out.println("received: " + textMessage.getText());
        } catch (JMSException ex){
            ex.printStackTrace();
        }
    }
    
}

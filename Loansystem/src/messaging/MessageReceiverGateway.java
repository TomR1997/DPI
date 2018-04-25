/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.util.Observer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Tomt
 */
public class MessageReceiverGateway implements MessageListener {

    private Connection connection;
    private Session session;
    private MessageConsumer consumer;
    private Observer observer;

    protected MessageReceiverGateway() {
        super();
    }

    public static MessageReceiverGateway newGateWay(Observer observer) throws NamingException, JMSException {
        MessageReceiverGateway gateway = new MessageReceiverGateway();
        gateway.initialize(observer);
        return gateway;
    }

    protected void initialize(Observer observer) throws NamingException, JMSException {
        this.observer = observer;
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
        props.setProperty("trustAllPackages", "true");
        Context jndiContext;

        jndiContext = new InitialContext(props);
        ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
                .lookup("ConnectionFactory");
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = (Destination) jndiContext.lookup("update");
        consumer = session.createConsumer(destination);
        consumer.setMessageListener(this);
    }

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String newState = textMessage.getText();
            update(newState);
        } catch (JMSException ex) {
            Logger.getLogger(MessageReceiverGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void attach() throws JMSException{
        connection.start();
    }
    
    public void detach() throws JMSException{
        if (connection != null){
            connection.stop();
            connection.close();
        }
    }
    
    private void update(String newState){
        //observer.update(newState);
    }

}

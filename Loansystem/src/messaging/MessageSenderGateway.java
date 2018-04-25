/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Tomt
 */
public class MessageSenderGateway {

    private Connection connection;
    private Session session;
    private MessageProducer producer;

    protected MessageSenderGateway() {
        super();
    }

    public MessageSenderGateway newGateway() throws JMSException, NamingException {
        MessageSenderGateway gateway = new MessageSenderGateway();
        gateway.initialize();
        return gateway;
    }

    protected void initialize() throws JMSException, NamingException {
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
        producer = session.createProducer(destination);

        connection.start();
    }
    
    public void notify(String state) throws JMSException{
        TextMessage message = session.createTextMessage(state);
        producer.send(message);
    }
    
    public void release() throws JMSException{
        if (connection != null){
            connection.stop();
            connection.close();
        }
    }
}

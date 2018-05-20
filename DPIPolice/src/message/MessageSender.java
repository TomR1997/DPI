/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Tomt
 */
public class MessageSender {

    Connection connection;
    Session session;

    Destination sendDestination;
    MessageProducer producer;

    public MessageSender(String queue, String topic) {
        try {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:8161");

            props.put(("queue." + queue), topic);

            Context jndiContext = new InitialContext(props);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
                    .lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // connect to the sender destination
            sendDestination = (Destination) jndiContext.lookup(queue);
            producer = session.createProducer(sendDestination);

        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    public String sendMessage(String content) {
        try {
            Message msg = session.createTextMessage(content);
            producer.send(msg);
            return msg.getJMSMessageID();
        } catch (JMSException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String sendMessage(String content, String correlationID) {
        try{
            Message msg = session.createTextMessage(content);
            msg.setJMSCorrelationID(correlationID);
            producer.send(msg);
            return msg.getJMSMessageID();
        } catch (JMSException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}

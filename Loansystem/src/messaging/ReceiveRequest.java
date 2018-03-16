/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

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
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Tomt
 */
public class ReceiveRequest {

    private Connection connection; // to connect to the JMS
    private Session session; // session for creating consumers

    private Destination receiveDestination; //reference to a queue/topic destination
    private MessageConsumer consumer; // for receiving messages

    public void receiveMessage() {
        try {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
            // connect to the Destination called “myFirstChannel”
            // queue or topic: “queue.myFirstDestination” or “topic.myFirstDestination”
            props.put(("queue.myFirstDestination"), " myFirstDestination");

            Context jndiContext = new InitialContext(props);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
                    .lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // connect to the receiver destination
            receiveDestination = (Destination) jndiContext.lookup("myFirstDestination");
            consumer = session.createConsumer(receiveDestination);

            connection.start();

        } catch (NamingException ex) {
            Logger.getLogger(ReceiveRequest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JMSException ex) {
            Logger.getLogger(ReceiveRequest.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message msg) {
                    System.out.println("received message: " + msg);
                }
            });
        } catch (JMSException ex){
            ex.printStackTrace();
        }
    }
}

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
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import loanbroker.models.LoanRequest;

/**
 *
 * @author Tomt
 */


public class SendRequest {

    private MessageProducer producer;
    private Connection connection;
    private Destination destination;
    private Session session;

    public void sendMessage(LoanRequest request) {
        try {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            // connect to the Destination called “myFirstChannel”
            // queue or topic: “queue.myFirstDestination” or “topic.myFirstDestination”
            props.put(("queue.myFirstDestination"), "myFirstDestination");

            Context jndiContext = new InitialContext(props);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
                    .lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // connect to the sender destination
            destination = (Destination) jndiContext.lookup("myFirstDestination");
            producer = session.createProducer(destination);

            String body = "Hello, this is my first message!"; //or serialize an object!  
            // create a text message
            Message msg = session.createTextMessage(body);
            ObjectMessage omsg = session.createObjectMessage(request);
            // send the message     
            producer.send(omsg);

        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }
}

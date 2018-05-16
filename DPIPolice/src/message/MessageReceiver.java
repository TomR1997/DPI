/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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
import observer.Observable;
import observer.Observer;

/**
 *
 * @author Tomt
 */
public class MessageReceiver implements Observable{

    private Connection connection;
    private Session session;

    private Destination receiveDestination;
    private MessageConsumer consumer = null;

    private List<Observer> observers = new ArrayList<>();

    public MessageReceiver(String queue, String topic) {

        try {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:8161");
            props.put("queue." + queue, topic); // "queue.requestQueue" "bankRequest"

            Context jndiContext = new InitialContext(props);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
                    .lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            receiveDestination = (Destination) jndiContext.lookup(queue);
            consumer = session.createConsumer(receiveDestination);

            /*consumer.setMessageListener(new MessageListener() {
            }*/

            connection.start(); // this is needed to start receiving messages
            System.out.println("Starting");

        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    public MessageConsumer getConsumer() {
        return consumer;
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Object... args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

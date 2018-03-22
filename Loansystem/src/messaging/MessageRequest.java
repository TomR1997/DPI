/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import abnamro.models.BankInterestReply;
import abnamro.models.BankInterestRequest;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import loanbroker.models.LoanReply;
import loanbroker.models.LoanRequest;

/**
 *
 * @author Tomt
 */
public class MessageRequest {

    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;

    private Queue lRequestQueue;
    private Queue lReplyQueue;
    private Queue bRequestQueue;
    private Queue bReplyQueue;

    private void connect() {
        try {
            if (connection != null) {
                return;
            }

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

            lRequestQueue = session.createQueue("lRequestQueue");
            lReplyQueue = session.createQueue("lReplyQueue");
            bRequestQueue = session.createQueue("bRequestQueue");
            bReplyQueue = session.createQueue("bReplyQueue");

        } catch (NamingException | JMSException ex) {
            Logger.getLogger(MessageRequest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void disconnect() {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (JMSException ex) {
            Logger.getLogger(MessageRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String send(IMessageRequest msgRequest) {
        try {
            connect();
            createProducer(msgRequest);
            return sendMessage(msgRequest);
        } catch (JMSException ex) {
            Logger.getLogger(MessageRequest.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            disconnect();
        }
    }

    public String send(IMessageRequest msgRequest, String correlationId) {
        try {
            connect();
            createProducer(msgRequest);
            return sendMessage(msgRequest, correlationId);
        } catch (JMSException ex) {
            Logger.getLogger(MessageRequest.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            disconnect();
        }
    }

    private String sendMessage(IMessageRequest msgRequest, String correlationId) throws JMSException {
        ObjectMessage message = session.createObjectMessage();
        message.setObject(msgRequest);
        message.setJMSCorrelationID(correlationId);
        producer.send(message);
        return message.getJMSMessageID();
    }

    private String sendMessage(IMessageRequest msgRequest) throws JMSException {
        ObjectMessage message = session.createObjectMessage();
        message.setObject(msgRequest);
        producer.send(message);
        return message.getJMSMessageID();
    }

    private void createProducer(IMessageRequest msgRequest) throws JMSException {
        if (msgRequest instanceof LoanRequest) {
            producer = session.createProducer(lRequestQueue);
        } else if (msgRequest instanceof LoanReply) {
            producer = session.createProducer(lReplyQueue);
        } else if (msgRequest instanceof BankInterestRequest) {
            producer = session.createProducer(bRequestQueue);
        } else if (msgRequest instanceof BankInterestReply) {
            producer = session.createProducer(bReplyQueue);
        }
    }

    public void receive(Class msgObjType, MessageListener listener) {
        try {
            connect();
            createConsumer(msgObjType);
            consumer.setMessageListener(listener);
            connection.start();
        } catch (JMSException ex) {
            Logger.getLogger(MessageRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createConsumer(Class msgObjType) throws JMSException {
        if (msgObjType.equals(LoanRequest.class)) {
            consumer = session.createConsumer(lRequestQueue);
        } else if (msgObjType.equals(LoanReply.class)) {
            consumer = session.createConsumer(lReplyQueue);
        } else if (msgObjType.equals(BankInterestRequest.class)) {
            consumer = session.createConsumer(bRequestQueue);
        } else if (msgObjType.equals(BankInterestReply.class)) {
            consumer = session.createConsumer(bReplyQueue);
        }
    }
}

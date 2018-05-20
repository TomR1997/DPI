/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

import client.models.ClientReply;
import client.models.ClientRequest;
import java.util.ArrayList;
import java.util.List;
import message.MessageReceiver;
import message.MessageSender;
import observer.Observable;
import observer.Observer;

/**
 *
 * @author Tomt
 */
public class ClientGateway implements Observer, Observable {

    private List<Observer> observers = new ArrayList<>();
    private MessageReceiver receiver;
    private MessageSender sender;
    private ClientSerializer serializer;

    public ClientGateway(String receiverTopic, String senderTopic) {
        this.receiver = new MessageReceiver("destination", receiverTopic);
        this.sender = new MessageSender("destination", senderTopic);
        this.serializer = new ClientSerializer();
        
        receiver.addObserver(this);
    }
    
    public void sendReply(ClientReply reply, String correlationId){
        sender.sendMessage(serializer.ReplyToString(reply), correlationId);
    }
    
    public void receiveRequest(String content, String correlationId){
        ClientRequest request = (ClientRequest) serializer.StringToRequest(content);
        notifyObservers(request, correlationId);
    }

    @Override
    public void update(Object... args) {
        String[] result = new String[]{args[0].toString(), args[1].toString()};
        if (result[0].startsWith("Request")) {
            receiveRequest(result[0], result[1]);
        }
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
        for (Observer o : observers) {
            o.update(args);
        }
    }
}

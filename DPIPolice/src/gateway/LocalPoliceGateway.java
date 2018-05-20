/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import localpolice.models.LocalPoliceReply;
import localpolice.models.LocalPoliceRequest;
import message.MessageReceiver;
import message.MessageSender;
import observer.Observable;
import observer.Observer;

/**
 *
 * @author Tomt
 */
public class LocalPoliceGateway implements Observer, Observable {

    private List<Observer> observers = new ArrayList<>();
    private MessageReceiver receiver;
    private MessageSender sender;
    private MessageSender register;
    private ISerializer serializer;
    private Map<LocalPoliceRequest, String> correlations = new HashMap<>();

    public LocalPoliceGateway(String registration, String senderTopic, String receiverTopic) {
        register = new MessageSender("destination", registration);
        register.sendMessage("Registration;;" + registration + ";;" + receiverTopic + ";;" + senderTopic);
        sender = new MessageSender("destination", senderTopic);
        receiver = new MessageReceiver("destination", receiverTopic);
        serializer = new LocalPoliceSerializer();
        receiver.addObserver(this);
    }

    public void receiveRequest(String content, String correlationId){
        LocalPoliceRequest request = (LocalPoliceRequest) serializer.StringToRequest(content);
        correlations.put(request, correlationId);
        notifyObservers(request);
    }
    
    public void sendReply(LocalPoliceRequest request, LocalPoliceReply reply){
        sender.sendMessage(serializer.ReplyToString(reply), correlations.get(request));
    }
    
    @Override
    public void update(Object... args) {
       String[] result = new String[]{args[0].toString(), args[1].toString()};
       receiveRequest(result[0], result[1]);
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

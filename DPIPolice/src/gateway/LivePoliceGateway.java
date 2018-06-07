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
import livepolice.models.LivePoliceReply;
import livepolice.models.LivePoliceRequest;
import message.MessageReceiver;
import message.MessageSender;
import observer.Observable;
import observer.Observer;

/**
 *
 * @author Tomt
 */
public class LivePoliceGateway implements Observer, Observable {
    private List<Observer> observers = new ArrayList<>();
    private MessageSender sender;
    private MessageReceiver receiver;
    private ISerializer serializer;
    private Map<LivePoliceRequest, String> correlations = new HashMap<>();

    public LivePoliceGateway(String senderTopic, String receiverTopic) {
        sender = new MessageSender("destination", senderTopic);
        receiver = new MessageReceiver("destination", receiverTopic);
        serializer = new LivePoliceSerializer();
        
        receiver.addObserver(this);
    }
    
    public void receiveRequest(String content, String correlationId){
        LivePoliceRequest request = (LivePoliceRequest) serializer.StringToRequest(content);
        correlations.put(request, correlationId);
        notifyObservers(request);
        sendReply(new LivePoliceReply(true, "smth", request.getLicencePlate()), correlationId);
        //start scan
    }
    
    public void sendReply(LivePoliceReply reply, String correlationId){
        sender.sendMessage(serializer.ReplyToString(reply), correlationId);
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
        for(Observer o : observers){
            o.update(args);
        }
    }
    
}

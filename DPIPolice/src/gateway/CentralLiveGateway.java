/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

import java.util.ArrayList;
import java.util.List;
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
public class CentralLiveGateway implements Observer, Observable {

    private List<Observer> observers = new ArrayList<>();
    private MessageSender sender;
    private MessageReceiver receiver;
    private ISerializer serializer;

    public CentralLiveGateway(String senderTopic, String receiverTopic) {
        sender = new MessageSender("destination", senderTopic);
        receiver = new MessageReceiver("destination", receiverTopic);
        serializer = new LivePoliceSerializer();
        
        receiver.addObserver(this);
    }

    public void sendRequest(LivePoliceRequest request, String correlationId){
        sender.sendMessage(serializer.RequestToString(request));
    }
    
    public void receiveReply(String content, String correlationId){
        LivePoliceReply reply = (LivePoliceReply) serializer.StringToReply(content);
        notifyObservers(reply, correlationId);
    }
    
    @Override
    public void update(Object... args) {
        String[] result = new String[]{args[0].toString(), args[1].toString()};
        if(result[0].startsWith("Reply")){
            receiveReply(result[0], result[1]);
        }
    }

    @Override
    public void addObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        this.observers.remove(o);
    }

    @Override
    public void notifyObservers(Object... args) {
        for (Observer o : observers) {
            o.update(args);
        }
    }

}

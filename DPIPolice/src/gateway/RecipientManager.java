package gateway;

import client.models.ClientReply;
import client.models.ClientRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import localpolice.models.LocalPoliceReply;
import localpolice.models.LocalPoliceRequest;
import message.LocalPoliceReplyManager;
import message.MessageReceiver;
import observer.Observable;
import observer.Observer;

public class RecipientManager implements Observable, Observer {

    private List<Observer> observers = new ArrayList<>();
    
    private HashMap<String, ClientRequest> correlations = new HashMap<>();
    private HashMap<String, LocalPoliceReplyManager> resultsForCorrelationID = new HashMap<>();

    private MessageReceiver receiver;

    private List<Recipient> recipients = new ArrayList<>();
    private CentralPoliceGateway gateway;

    public RecipientManager() {
        this.receiver = new MessageReceiver("destination", "registration");
        gateway = new CentralPoliceGateway("clientRequest", "clientReply");

        receiver.addObserver(this);
        gateway.addObserver(this);
    }

    public void receiveRegistraion(String registration) {
        String[] strings = registration.split(";;");
        System.out.println("registration received: "+Arrays.toString(strings));
        Recipient r = new Recipient(strings[1], strings[2], strings[3], strings[4]);
        recipients.add(r);
        r.addObserverToGateway(this);
    }

    public void receiveRequest(ClientRequest request, String correlationID) {
        correlations.put(correlationID, request);
        System.out.println("request received:" + request.toString());
        notifyObservers(request);
        sendRequestToRecipients(new LocalPoliceRequest(request.getLocation(), request.getLicenceplate()), correlationID);
    }
    
    public void sendRequestToRecipients(LocalPoliceRequest request, String correlationID){
        int expectedResultCount = 0;
        for(Recipient r : recipients){
            r.sendRequestToBank(request, correlationID);
            expectedResultCount++;
        }
        
        if (expectedResultCount == 0){
            notifyObservers(correlations.get(correlationID), new LocalPoliceReply(false, "", "None"));
            sendReplyToClient(new ClientReply(false, "", "None"), correlationID);
            return;
        }
        
        resultsForCorrelationID.put(correlationID, new LocalPoliceReplyManager(expectedResultCount));
    }
    
    public void receiveReply(LocalPoliceReply reply, String correlationID){
        LocalPoliceReplyManager localPoliceReplyManager = resultsForCorrelationID.get(correlationID);
        localPoliceReplyManager.newReply(reply);
        if(localPoliceReplyManager.isCompleted()){
            LocalPoliceReply bestReply = localPoliceReplyManager.getBestReply();
            notifyObservers(correlations.get(correlationID), bestReply);
            sendReplyToClient(new ClientReply(bestReply.isFound(), bestReply.getLocalPoliceId(), bestReply.getLocation()), correlationID);
        }
    }
    
    public void sendReplyToClient(ClientReply reply, String correlationID){
        gateway.sendReply(reply, correlationID);
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

    @Override
    public void update(Object... args) {
        if (args[0] instanceof LocalPoliceReply) {
            receiveReply((LocalPoliceReply) args[0], args[1].toString());
        } else if (args[0] instanceof ClientRequest) {
            receiveRequest((ClientRequest) args[0], args[1].toString());
        } else if (args[0].toString().startsWith("Registration")) {
            receiveRegistraion(args[0].toString());
        }
    }
}

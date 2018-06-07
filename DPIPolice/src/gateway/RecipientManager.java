package gateway;

import client.models.ClientReply;
import client.models.ClientRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import livepolice.models.LivePoliceReply;
import livepolice.models.LivePoliceRequest;
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
    private CentralLiveGateway liveGateway;

    public RecipientManager() {
        this.receiver = new MessageReceiver("destination", "registration");
        gateway = new CentralPoliceGateway("clientRequest", "clientReply");
        liveGateway = new CentralLiveGateway("livePoliceRequest", "livePoliceReply");

        receiver.addObserver(this);
        gateway.addObserver(this);
        liveGateway.addObserver(this);
    }

    public void receiveRegistraion(String registration) {
        String[] strings = registration.split(";;");
        Recipient r = new Recipient(strings[0], strings[1], strings[2], strings[3]);
        recipients.add(r);
        r.addObserverToGateway(this);
    }

    public void receiveRequest(ClientRequest request, String correlationID) {
        correlations.put(correlationID, request);
        notifyObservers(request);
        sendRequestToRecipients(new LocalPoliceRequest(request.getLocation(), request.getLicenceplate()), correlationID);
    }
    
    public void sendRequestToRecipients(LocalPoliceRequest request, String correlationID){
        int expectedResultCount = 0;
        for(Recipient r : recipients){
            if(r.isQualified(r, request)){
                r.sendRequestToLocalPolice(request, correlationID);
                expectedResultCount++;
            }
        }
        
        if (expectedResultCount == 0){
            notifyObservers(correlations.get(correlationID), new LocalPoliceReply(false, "", "None", "None"));
            sendReplyToClient(new ClientReply(false, "None", "None"), correlationID);
            return;
        }
        
        resultsForCorrelationID.put(correlationID, new LocalPoliceReplyManager(expectedResultCount));
    }
    
    public void receiveReply(LocalPoliceReply reply, String correlationID){
        LocalPoliceReplyManager localPoliceReplyManager = resultsForCorrelationID.get(correlationID);
        localPoliceReplyManager.newReply(reply);
        if(localPoliceReplyManager.isCompleted()){
            LocalPoliceReply bestReply = localPoliceReplyManager.getBestReply();
            if (bestReply == null){
                sendLivePoliceRequest(new LivePoliceRequest(reply.getLicencePlate()), correlationID);
            } else {
                notifyObservers(correlations.get(correlationID), bestReply);
                sendReplyToClient(new ClientReply(bestReply.isFound(), bestReply.getLocalPoliceId(), bestReply.getLocation()), correlationID);
            }
        }
    }
    
    public void sendReplyToClient(ClientReply reply, String correlationID){
        gateway.sendReply(reply, correlationID);
    }
    
    public void sendLivePoliceRequest(LivePoliceRequest request, String correlationID){
        liveGateway.sendRequest(request, correlationID);
    }
    
    public void receiveLiveReply(LivePoliceReply reply, String correlationId){
        notifyObservers(correlations.get(correlationId), reply);
        sendReplyToClient(new ClientReply(reply.isFound(), reply.getLocalPoliceId(), reply.getLocation()), correlationId);
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
        } else if (args[0] instanceof LivePoliceReply){
            receiveLiveReply((LivePoliceReply) args[0], args[1].toString());
        }
    }

}

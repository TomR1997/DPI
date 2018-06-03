package gateway;

import localpolice.models.LocalPoliceRequest;
import observer.Observer;

public class Recipient {

    private String name;
    private String location;
    private CentralLocalGateway gateway;

    public Recipient(String name,String location, String senderTopic, String receiverTopic) {
        this.name = name;
        this.location = location;
        gateway = new CentralLocalGateway(senderTopic, receiverTopic);
    }
    
    public void sendRequestToLocalPolice(LocalPoliceRequest request, String correlationID){
        gateway.sendRequest(request, correlationID);
    }
    
    public void addObserverToGateway(Observer o){
        gateway.addObserver(o);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

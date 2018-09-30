package message;

import client.models.ClientRequest;
import livepolice.models.LivePoliceReply;
import livepolice.models.LivePoliceRequest;
import localpolice.models.LocalPoliceReply;
import localpolice.models.LocalPoliceRequest;

public class JListLine {

    private ClientRequest clientRequest;
    private LocalPoliceRequest localPoliceRequest;
    private LocalPoliceReply localPoliceReply;
    private LivePoliceRequest livePoliceRequest;
    private LivePoliceReply livePoliceReply;

    public LivePoliceRequest getLivePoliceRequest() {
        return livePoliceRequest;
    }

    public void setLivePoliceRequest(LivePoliceRequest livePoliceRequest) {
        this.livePoliceRequest = livePoliceRequest;
    }

    public LivePoliceReply getLivePoliceReply() {
        return livePoliceReply;
    }

    public void setLivePoliceReply(LivePoliceReply livePoliceReply) {
        this.livePoliceReply = livePoliceReply;
    }

    public JListLine(ClientRequest clientRequest) {
        this.setClientRequest(clientRequest);
    }

    public ClientRequest getClientRequest() {
        return clientRequest;
    }

    public void setClientRequest(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }

    public LocalPoliceRequest getLocalPoliceRequest() {
        return localPoliceRequest;
    }

    public void setLocalPoliceRequest(LocalPoliceRequest localPoliceRequest) {
        this.localPoliceRequest = localPoliceRequest;
    }

    public LocalPoliceReply getLocalPoliceReply() {
        return localPoliceReply;
    }

    public void setLocalPoliceReply(LocalPoliceReply localPoliceReply) {
        this.localPoliceReply = localPoliceReply;
    }

    @Override
    public String toString() {
        return clientRequest.toString() + " || " + (livePoliceReply != null ? livePoliceReply.toString() : ((localPoliceReply != null) ? localPoliceReply.toString() : "waiting for reply..."));
    }

}

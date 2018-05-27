package message;

import client.models.ClientRequest;
import localpolice.models.LocalPoliceReply;
import localpolice.models.LocalPoliceRequest;

public class JListLine {
	
	private ClientRequest clientRequest;
	private LocalPoliceRequest localPoliceRequest;
	private LocalPoliceReply localPoliceReply;

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
		return clientRequest.toString() + " || " + ((localPoliceReply != null) ? localPoliceReply.toString() : "waiting for reply...");
	}

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.models;

/**
 *
 * @author Tomt
 */
public class ClientReply {
    private boolean found;
    private String location;
    private String localPoliceId;

    public ClientReply(boolean found, String localPoliceId, String location) {
        super();
        this.found = found;
        this.localPoliceId = localPoliceId;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public String getLocalPoliceId() {
        return localPoliceId;
    }

    public void setLocalPoliceId(String localPoliceId) {
        this.localPoliceId = localPoliceId;
    }
    
    @Override
    public String toString(){
        return "Request;;" + found + ";;" + localPoliceId + ";;" + location;
    }
}

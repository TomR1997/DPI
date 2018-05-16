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
    private String localPoliceId;

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
    
    
}

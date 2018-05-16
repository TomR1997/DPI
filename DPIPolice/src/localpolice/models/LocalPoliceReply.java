/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localpolice.models;

/**
 *
 * @author Tomt
 */
public class LocalPoliceReply {
    private boolean found;
    private String location;
    private String localPoliceId;

    public LocalPoliceReply(boolean found, String localPoliceId, String location) {
        super();
        this.found = found;
        this.localPoliceId = localPoliceId;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
}

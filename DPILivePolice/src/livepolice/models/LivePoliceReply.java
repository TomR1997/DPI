/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package livepolice.models;

/**
 *
 * @author Tomt
 */
public class LivePoliceReply {

    private boolean found;
    private String location;
    private String localPoliceId;

    public LivePoliceReply(boolean found, String location, String localPoliceId) {
        this.found = found;
        this.location = location;
        this.localPoliceId = localPoliceId;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocalPoliceId() {
        return localPoliceId;
    }

    public void setLocalPoliceId(String localPoliceId) {
        this.localPoliceId = localPoliceId;
    }

    @Override
    public String toString() {
        return "Reply;;" + found + ";;" + localPoliceId + ";;" + location;
    }

}

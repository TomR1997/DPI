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
public class ClientRequest {
    private String clientName;
    private String location;
    private String licenceplate;
    
    public ClientRequest(String location, String licenceplate, String clientName){
        super();
        this.location = location;
        this.licenceplate = licenceplate;
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLicenceplate() {
        return licenceplate;
    }

    public void setLicenceplate(String licenceplate) {
        this.licenceplate = licenceplate;
    }
    
    @Override
    public String toString() {
        return "Request;;" + location + ";;" + licenceplate + ";;" + clientName;
    }
}

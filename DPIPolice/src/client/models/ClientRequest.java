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
    private String location;
    private String licenceplate;
    
    public ClientRequest(String location, String licenceplate){
        super();
        this.location = location;
        this.location = licenceplate;
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
        return "location=" + location + " licenceplate=" + licenceplate;
    }
}

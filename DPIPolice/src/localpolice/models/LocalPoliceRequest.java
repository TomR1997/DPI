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
public class LocalPoliceRequest {
    private String location;
    private String licencePlate;

    public LocalPoliceRequest(String location, String licencePlate) {
        super();
        this.location = location;
        this.licencePlate = licencePlate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }
    
    @Override
    public String toString(){
        return "Request;;" + location + ";;" + licencePlate;
    }
    
}

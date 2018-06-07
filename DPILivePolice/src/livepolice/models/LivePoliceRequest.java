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
public class LivePoliceRequest {
    private String licencePlate;

    public LivePoliceRequest(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }
    
    @Override
    public String toString(){
        return "Request;;" + licencePlate;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

import java.util.Arrays;
import localpolice.models.LocalPoliceReply;
import localpolice.models.LocalPoliceRequest;

/**
 *
 * @author Tomt
 */
public class LocalPoliceSerializer implements ISerializer<LocalPoliceRequest, LocalPoliceReply>{

    @Override
    public String RequestToString(LocalPoliceRequest request) {
        return request.toString();
    }

    @Override
    public LocalPoliceRequest StringToRequest(String string) {
        String[] strings = string.split(";;");
        return new LocalPoliceRequest(strings[1], strings[2]);
    }

    @Override
    public String ReplyToString(LocalPoliceReply reply) {
        return reply.toString();
    }

    @Override
    public LocalPoliceReply StringToReply(String string) {
        String[] strings = string.split(";;");
        return new LocalPoliceReply(Boolean.parseBoolean(strings[1]), strings[2], strings[3]);
    }
    
}

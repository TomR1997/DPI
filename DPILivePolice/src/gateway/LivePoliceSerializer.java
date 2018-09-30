/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

import livepolice.models.LivePoliceReply;
import livepolice.models.LivePoliceRequest;

/**
 *
 * @author Tomt
 */
public class LivePoliceSerializer implements ISerializer<LivePoliceRequest, LivePoliceReply>{

    @Override
    public String RequestToString(LivePoliceRequest request) {
        return request.toString();
    }

    @Override
    public LivePoliceRequest StringToRequest(String string) {
        String[] strings = string.split(";;");
        return new LivePoliceRequest(strings[1]);
    }

    @Override
    public String ReplyToString(LivePoliceReply reply) {
        return reply.toString();
    }

    @Override
    public LivePoliceReply StringToReply(String string) {
        String[] strings = string.split(";;");
        return new LivePoliceReply(Boolean.parseBoolean(strings[1]), strings[2], strings[3]);
    }
    
}

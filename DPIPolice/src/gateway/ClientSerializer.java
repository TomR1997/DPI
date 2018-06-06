/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

import client.models.ClientReply;
import client.models.ClientRequest;

/**
 *
 * @author Tomt
 */
public class ClientSerializer implements ISerializer<ClientRequest, ClientReply>{

    @Override
    public String RequestToString(ClientRequest request) {
        return request.toString();
    }

    @Override
    public ClientRequest StringToRequest(String string) {
        String[] strings = string.split(";;");
        return new ClientRequest(strings[1], strings[2], strings[3]);
    }

    @Override
    public String ReplyToString(ClientReply reply) {
        return reply.toString();
    }

    @Override
    public ClientReply StringToReply(String string) {
        String[] strings  = string.split(";;");
        return new ClientReply(Boolean.parseBoolean(strings[1]), strings[2], strings[3]);
    }

   
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

/**
 *
 * @author Tomt
 * @param <RequestT>
 * @param <ReplyT>
 */
public class RequestReply<RequestT, ReplyT> {

    private RequestT request;
    private ReplyT reply;

    public RequestReply(RequestT request, ReplyT reply) {
        setRequest(request);
        setReply(reply);
    }

    public RequestT getRequest() {
        return request;
    }

    public void setRequest(RequestT request) {
        this.request = request;
    }

    public ReplyT getReply() {
        return reply;
    }

    public void setReply(ReplyT reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return request.toString() + "  --->  " + ((reply != null) ? reply.toString() : "waiting for reply...");
    }
}

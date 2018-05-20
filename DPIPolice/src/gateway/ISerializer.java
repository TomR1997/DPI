/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

/**
 *
 * @author Tomt
 */
public interface ISerializer<requestT, replyT> {

    String RequestToString(requestT request);

    requestT StringToRequest(String string);

    String ReplyToString(replyT reply);

    replyT StringToReply(String string);
}

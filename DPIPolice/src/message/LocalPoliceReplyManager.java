/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import localpolice.models.LocalPoliceReply;

public class LocalPoliceReplyManager {
    
    private int expectedReplyCount;
    private int replyCount = 0;
    private LocalPoliceReply bestReply = null;

    public LocalPoliceReplyManager(int expectedReplyCount) {
        this.expectedReplyCount = expectedReplyCount;
    }
    
    public void newReply(LocalPoliceReply reply){
        bestReply = reply;
        replyCount++;
    }

    public boolean isCompleted(){
        return replyCount >= expectedReplyCount;
    }
    
    public LocalPoliceReply getBestReply() {
        return bestReply;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import livepolice.models.LivePoliceReply;

/**
 *
 * @author Tomt
 */
public class CarReplyManager {
    private int expectedReplyCount;
    private int replyCount = 0;
    private LivePoliceReply bestReply = null;
    private boolean found = false;

    public CarReplyManager(int expectedReplyCount) {
        this.expectedReplyCount = expectedReplyCount;
    }
    
    public void newReply(LivePoliceReply reply){
        if (reply.isFound()){
            found = true;
            bestReply = reply;
        }
        
        replyCount++;
    }
    
    public boolean isCompleted(){
        return replyCount >= expectedReplyCount;
    }

    public LivePoliceReply getBestReply() {
        return bestReply;
    }

    public boolean isFound() {
        return found;
    }
    
}

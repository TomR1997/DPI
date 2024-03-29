package abnamro.models;

import messaging.IMessageRequest;

/**
 * This class stores information about the bank reply
 *  to a loan request of the specific client
 * 
 */
public class BankInterestReply implements IMessageRequest{

    private double interest; // the loan interest
    private String bankId; // the nunique quote Id
    
    public BankInterestReply() {
        this.interest = 0;
        this.bankId = "";
    }
    
    public BankInterestReply(double interest, String quoteId) {
        this.interest = interest;
        this.bankId = quoteId;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public String getQuoteId() {
        return bankId;
    }

    public void setQuoteId(String quoteId) {
        this.bankId = quoteId;
    }

    public String toString() {
        return this.bankId + ";" + this.interest;
    }
}

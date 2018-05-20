/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

import java.util.ArrayList;
import java.util.List;
import message.MessageReceiver;
import message.MessageSender;
import observer.Observable;
import observer.Observer;

/**
 *
 * @author Tomt
 */
public class CentralPoliceGateway implements Observer, Observable {

    private List<Observer> observers = new ArrayList<>();
    private MessageReceiver receiver;
    private MessageSender sender;

    @Override
    public void update(Object... args) {

    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Object... args) {
        for (Observer o : observers) {
            o.update(args);
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import livepolice.models.LivePoliceReply;
import livepolice.models.LivePoliceRequest;
import message.CarReplyManager;
import message.MessageReceiver;
import message.MessageSender;
import observer.Observable;
import observer.Observer;

/**
 *
 * @author Tomt
 */
public class LivePoliceGateway implements Observer, Observable {

    private List<Observer> observers = new ArrayList<>();
    private MessageSender sender;
    private MessageReceiver receiver;
    private MessageSender carSender;
    private MessageReceiver carReceiver;

    private ISerializer serializer;
    private Map<LivePoliceRequest, String> correlations = new HashMap<>();
    private Map<String, CarReplyManager> resultsForCorrelationID = new HashMap<>();
    private int numberOfCars = 5;

    public LivePoliceGateway(String senderTopic, String receiverTopic) {
        sender = new MessageSender("destination", senderTopic);
        receiver = new MessageReceiver("destination", receiverTopic);

        carSender = new MessageSender("destination", "car");
        carReceiver = new MessageReceiver("destination", "car");

        serializer = new LivePoliceSerializer();

        receiver.addObserver(this);
    }

    public void receiveRequest(String content, String correlationId) {
        LivePoliceRequest request = (LivePoliceRequest) serializer.StringToRequest(content);
        correlations.put(request, correlationId);
        notifyObservers(request);
        //sendReply(new LivePoliceReply(true, "smth", request.getLicencePlate()), correlationId);
        initLiveScan(correlationId);
    }

    public void sendReply(LivePoliceReply reply, String correlationId) {
        sender.sendMessage(serializer.ReplyToString(reply), correlationId);
    }

    public void initLiveScan(String correlationId) {
        String licencePlate = "";
        for (int i = 0; i < numberOfCars; i++) {
            licencePlate = Integer.toString(i);
            sendCar("Car;;" + licencePlate, correlationId);
        }
        resultsForCorrelationID.put(correlationId, new CarReplyManager(numberOfCars));
    }

    public void sendCar(String licencePlate, String correlationId) {
        carSender.sendMessage(licencePlate, correlationId);
    }

    public void receiveCar(String content, String correlationId) {
        CarReplyManager carReplyManager = resultsForCorrelationID.get(correlationId);
        for (Entry<LivePoliceRequest, String> entry : correlations.entrySet()) {
            if (entry.getValue() == null ? correlationId == null : entry.getValue().equals(correlationId)) {
                if (entry.getKey().getLicencePlate().equals(getCarLicencePlate(content))) {
                    carReplyManager.newReply(new LivePoliceReply(true, "Maaskantje", "LocalMaaskantjeA"));
                    sendReply(carReplyManager.getBestReply(), correlationId);
                } else {
                    carReplyManager.newReply(new LivePoliceReply(false, "None", "None"));
                }
            }
        }

        if (carReplyManager.isCompleted()) {
            sendReply(new LivePoliceReply(false, "None", "None"), correlationId);
        }
    }

    public String getCarLicencePlate(String content) {
        String[] strings = content.split(";;");
        System.out.println(strings[1]);
        return strings[1];
    }

    @Override
    public void update(Object... args) {
        String[] result = new String[]{args[0].toString(), args[1].toString()};
        if (result[0].startsWith("Car")) {
            receiveCar(result[0], result[1]);
        } else {
            receiveRequest(result[0], result[1]);
        }
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

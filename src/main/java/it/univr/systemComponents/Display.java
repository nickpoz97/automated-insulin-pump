package it.univr.systemComponents;
import it.univr.states.InsulinStates;
import it.univr.states.SugarStates;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

public class Display {
    private final Queue<String> infoQueue;
    private static Calendar timestamp = null;

    private int sugarLevel;
    private int remainingInsulin;
    private String statusMessage;

    public Display(){
        if(timestamp == null){
            timestamp = Calendar.getInstance();
        }
        infoQueue = new LinkedList<>();
    }

    public void printData(int sugarLevel, int remainingInsulin, SugarStates sugarStatus, InsulinStates insulinStatus){
        this.sugarLevel = sugarLevel;
        this.remainingInsulin = remainingInsulin;
        this.statusMessage = interpretStatus(sugarStatus, insulinStatus);
        System.out.println(this);
        timestamp.add(Calendar.MINUTE, 10);
    }

    private String interpretStatus(SugarStates sugarStatus, InsulinStates insulinStatus) {
        String message = "";

        message += "Insulin: " + interpretInsulinStatus(insulinStatus) + '\n';
        message += "Sugar: " + interpretSugarStatus(sugarStatus);

        return message;
    }

    private String interpretInsulinStatus(InsulinStates insulinStatus) {
        if(insulinStatus == InsulinStates.LOW_RESERVE){
            return "WARNING! Low remaining insulin";
        }
        else if(insulinStatus == InsulinStates.EMPTY){
            return "DANGER! Empty reservoir, fill it!";
        }
        else{
            return "Good";
        }
    }

    private String interpretSugarStatus(SugarStates sugarStatus) {
        if(sugarStatus == SugarStates.LOW_SUGAR){
            return "WARNING! low sugar level";
        }
        else if(sugarStatus == SugarStates.HIGH_SUGAR){
            return "WARNING! high sugar level";
        }
        else if(sugarStatus == SugarStates.VERY_HIGH_SUGAR){
            return "DANGER! sugar level is lethally high! You have eaten too much sugar or no insuline available";
        }
        else if(sugarStatus == SugarStates.VERY_LOW_SUGAR){
            return "DANGER! sugar level is lethally low! Eat something with sugar";
        }
        else {
            return "Good";
        }
    }

    public void addInfo(String message){
        infoQueue.add(message);
    }

    private String extractInfos() {
        String infos = "";

        while (!infoQueue.isEmpty()) {
            infos = infos + infoQueue.poll() +'\n';
        }

        return infos;
    }

    public Calendar getTimestamp(){
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("Date: %td/%tm/%tY %tT\n", timestamp, timestamp, timestamp, timestamp) +
                "Sugar level: " + sugarLevel + '\n' +
                "Remaining insulin: " + remainingInsulin + '\n' +
                statusMessage + '\n' +
                extractInfos();
    }
}

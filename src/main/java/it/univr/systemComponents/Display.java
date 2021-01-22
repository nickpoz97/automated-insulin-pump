package it.univr.systemComponents;
import it.univr.states.InsulinStates;
import it.univr.states.SugarStates;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

public class Display {
    private Queue<String> infoQueue;
    private static Calendar calendar = null;

    public Display(){
        if(calendar == null){
            calendar = Calendar.getInstance();
        }
        infoQueue = new LinkedList<>();
    }

    public void printData(int sugar, int remainingInsulin, SugarStates sugarStatus, InsulinStates insulinStatus){
        String statusMessage = interpretStatus(sugarStatus, insulinStatus);
        System.out.println();
        System.out.format("Date: %td/%tm/%tY %tT\n", calendar,calendar,calendar,calendar);
        System.out.println("Sugar level: " + sugar);
        System.out.println("Remaining insulin: " + remainingInsulin);
        System.out.println(statusMessage);
        this.printInfos();
        System.out.println();
        calendar.add(Calendar.HOUR_OF_DAY, 1);
    }

    private String interpretStatus(SugarStates sugarStatus, InsulinStates insulinStatus) {
        String message = "";

        message += "Insulin: " + interpretInsulinStatus(insulinStatus) + '\n';
        message += "Sugar: " + interpretSugarStatus(sugarStatus);

        return message;
    }

    private String interpretInsulinStatus(InsulinStates insulinStatus) {
        if(insulinStatus == InsulinStates.LOWRESERVE){
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
        if(sugarStatus == SugarStates.LOWSUGAR){
            return "WARNING! low sugar level";
        }
        else if(sugarStatus == SugarStates.HIGHSUGAR){
            return "WARNING! high sugar level";
        }
        else if(sugarStatus == SugarStates.VERYHIGHSUGAR){
            return "DANGER! sugar level is lethally high! You have eaten too much sugar or no insuline available";
        }
        else if(sugarStatus == SugarStates.VERYLOWSUGAR){
            return "DANGER! sugar level is lethally low! Eat something with sugar";
        }
        else {
            return "Good";
        }
    }

    public void addInfo(String message){
        infoQueue.add(message);
    }

    private void printInfos() {
        while (!infoQueue.isEmpty()) {
            System.out.println(infoQueue.poll());
        }
    }
}

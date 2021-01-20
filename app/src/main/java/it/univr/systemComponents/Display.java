package it.univr.systemComponents;

import it.univr.bloodModels.BloodModel;
import it.univr.states.InsulinStates;
import it.univr.states.SugarStates;
import it.univr.systemComponents.InsulineReservoir;

import java.util.Calendar;
import java.util.Scanner;

public class Display {

    public void printData(int sugar, int remainingInsulin, SugarStates sugarStatus, InsulinStates insulinStatus){
        Calendar calendar = Calendar.getInstance();
        String statusMessage = interpretStatus(sugarStatus, insulinStatus);

        System.out.println();
        System.out.format("Date: %td/%tm/%tY %tT\n", calendar,calendar,calendar,calendar);
        System.out.println("Sugar level: " + sugar);
        System.out.println("Remaining insulin: " + remainingInsulin);
        System.out.println(statusMessage);
        System.out.println();
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
        else {
            return "GOOD";
        }
    }

    public void printError(String message){
        System.out.println("\n+message+\n");
    }
}

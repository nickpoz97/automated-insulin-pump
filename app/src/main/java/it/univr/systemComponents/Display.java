package it.univr.systemComponents;

import it.univr.bloodModels.BloodModel;
import it.univr.states.InsulinStates;
import it.univr.states.SugarStates;
import it.univr.systemComponents.InsulineReservoir;

import java.util.Calendar;
import java.util.Scanner;

public class Display {
    private static int lastDisplayId = -1;
    private final int displayId;
    // only used in interactive mode
    private static BloodModel displayBloodModel;
    private static InsulineReservoir displayInsulineReservoir;

    public Display(){
        lastDisplayId++;
        this.displayId = lastDisplayId;
    }

    public Display(BloodModel bloodModel, InsulineReservoir insulineReservoir){
        this();
        // only first display can be interactive (testing)
        if(this.displayId == 0) {
            displayInsulineReservoir = insulineReservoir;
            displayBloodModel = bloodModel;
        }
    }

    private boolean isInteractive(){
        return displayInsulineReservoir != null
                && displayBloodModel != null
                && this.displayId == 0;
    }

    public void printData(int sugar, int remainingInsulin, SugarStates sugarStatus, InsulinStates insulinStatus){
        Calendar calendar = Calendar.getInstance();
        String statusMessage = interpretStatus(sugarStatus, insulinStatus);

        System.out.println();
        System.out.println("Display id: " + displayId);
        System.out.format("Date: %td/%tm/%tY %tT", calendar);
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

    public void inputHandler() {
        if(this.isInteractive()){
            return;
        }

        Scanner keyboard = new Scanner(System.in);
        String choice = "_"; // ! c

        while(!choice.equals("c")) {
            printChoices();
            if (choice.equals("i")) {
                processReservoirFilling(keyboard);
            } else if (choice.equals("s")) {
                processSugarAddition(keyboard);
            }
            choice = keyboard.next();
        }
        keyboard.close();
    }

    private void printChoices() {
        System.out.println("Choose an action:");
        System.out.println("c) continue execution (1 minute time simulation)");
        System.out.println("i) fill insulin reservoir");
        System.out.println("s) add sugar");
        System.out.print("Your choice: ");
    }

    private void processReservoirFilling(Scanner keyboard) {
        System.out.print("Insert insulin amount (negative values are equal to 0): ");
        int value = Math.max(keyboard.nextByte(),0);
        displayInsulineReservoir.add(value);
    }

    private void processSugarAddition(Scanner keyboard) {
        System.out.print("Insert sugar amount (negative values are equal to 0): ");
        int value = Math.max(keyboard.nextByte(), 0);
        displayBloodModel.addSugar(value);
    }
}

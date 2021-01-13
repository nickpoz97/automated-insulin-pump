package it.univr.SystemComponents;

import java.util.Calendar;
import java.util.Scanner;

public class Display {
    private static int lastDisplayId = 0;
    private int displayId;

    public Display(){
        lastDisplayId++;
        this.displayId = lastDisplayId;
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
        // TODO complete the method
        return null;
    }

    public void printError(String message){
        System.out.println("\n+message+\n");
    }

    public void inputHandler() {
        System.out.println("Choose an action:");
        System.out.println("c) continue execution (1 minute time simulation)");
        System.out.println("i) fill insulin reservoir");
        System.out.println("s) add sugar");
        System.out.print("Your choice: ");
        try(Scanner keyboard = new Scanner(System.in)){
            String choice = keyboard.next();
            if(choice.equals("c")){
                return;
            }
            else if(choice.equals("i")){
                processReservoirFilling();
            }
            else if(choice.equals("s")){
                processSugarAddition();
            }
        }
    }

    private void processReservoirFilling() {
    }

    private void processSugarAddition() {
    }
}

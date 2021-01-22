package it.univr.systemComponents;

import it.univr.bloodModels.BloodModel;

import java.util.Scanner;

import static java.lang.System.exit;

public class InputHandler {
    private final BloodModel bloodModel;
    private final InsulinReservoir insulinReservoir;
    private static final Scanner keyboard = new Scanner(System.in);

    public InputHandler(BloodModel bloodModel, InsulinReservoir insulinReservoir){
        this.bloodModel = bloodModel;
        this.insulinReservoir = insulinReservoir;
    }

    public void processInput() {
        String choice = "_"; // ! c
        boolean sChosen = false;

        while (!choice.equals("c")) {
            printChoices();
            choice = keyboard.nextLine();
            if (choice.equals("i")) {
                processReservoirFilling();
            } else if (choice.equals("s")) {
                processSugarAddition(sChosen);
                sChosen = true;
            } else if (choice.equals("e")) {
                keyboard.close();
                exit(0);
            }
        }
    }

    private void printChoices() {
        System.out.println("Choose an action:");
        System.out.println("c) continue execution (1 minute time simulation)");
        System.out.println("i) fill insulin reservoir");
        System.out.println("s) add sugar");
        System.out.println("e) end simulation");
        System.out.print("Your choice: ");
    }

    private void processReservoirFilling() {
        System.out.print("Insert insulin amount (negative values are equal to 0): ");
        int value;
        try {
            value = Integer.parseInt(keyboard.nextLine());
            value = Math.max(value, 0);
        }
        catch (NumberFormatException e){
            value = 0;
        }
        insulinReservoir.add(value);
    }

    private void processSugarAddition(boolean alreadyChosen) {
        if(alreadyChosen){
            System.out.println("This option cannot be chosen more 2 than times in a row");
            return;
        }

        System.out.println("Insert sugar amount");
        System.out.print("negative values are equal to 0 and max allowed value is 20: ");
        int value;
        try {
            value = Integer.parseInt(keyboard.nextLine());
            value = Math.max(value, 0);
            value = Math.min(value, 20);
        }
        catch (NumberFormatException e){
            value = 0;
        }
        bloodModel.addSugar(value);
    }
}

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

        while (!choice.equals("c")) {
            printChoices();
            choice = keyboard.nextLine();
            if (choice.equals("i")) {
                processReservoirFilling(keyboard);
            } else if (choice.equals("s")) {
                processSugarAddition(keyboard);
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

    private void processReservoirFilling(Scanner keyboard) {
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

    private void processSugarAddition(Scanner keyboard) {
        System.out.print("Insert sugar amount (negative values are equal to 0): ");
        int value;
        try {
            value = Integer.parseInt(keyboard.nextLine());
            value = Math.max(value, 0);
        }
        catch (NumberFormatException e){
            value = 0;
        }
        bloodModel.addSugar(value);
    }
}

package it.univr.systemComponents;

import it.univr.bloodModels.BloodModel;

import java.util.Scanner;

public class InputHandler {
    private BloodModel bloodModel;
    private InsulineReservoir insulineReservoir;

    public InputHandler(BloodModel bloodModel, InsulineReservoir insulineReservoir){
        this.bloodModel = bloodModel;
        this.insulineReservoir = insulineReservoir;
    }

    public void processInput() {
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
        insulineReservoir.add(value);
    }

    private void processSugarAddition(Scanner keyboard) {
        System.out.print("Insert sugar amount (negative values are equal to 0): ");
        int value = Math.max(keyboard.nextByte(), 0);
        bloodModel.addSugar(value);
    }
}

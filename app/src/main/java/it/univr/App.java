/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package it.univr;

import java.util.Calendar;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        int sugarLevel = 100;
        int incrementValue = 0;
        int incrementRate = 0;
        int insulinAmount = 30;
        new AutomatedInsulinPump(sugarLevel, incrementValue, incrementRate, insulinAmount, true);
    }
}

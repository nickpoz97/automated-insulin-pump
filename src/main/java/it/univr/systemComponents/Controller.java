package it.univr.systemComponents;

import it.univr.exceptions.InsulinAvailabilityException;
import it.univr.states.InsulinStates;
import it.univr.states.SugarStates;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class Controller {
    private static final int lowerSugarBound = 80;
    private static final int upperSugarBound = 140;
    private static final int veryHighSugarLevel = 200;
    private static final int veryLowSugarLevel = 20;
    private static final int lowerInsulinBound = 50;

    private final Pump pump;
    private final List<Display> displays = new ArrayList<>(2);
    private final SugarSensor sugarSensor;

    private SugarStates sugarState;
    private InsulinStates insulinState;

    private int remainingInsulin;
    private int lastMeasurement;
    private int increment;

    private InputHandler inputHandler = null;

    public Controller(Pump pump, Display display, SugarSensor sugarSensor) {
        this.pump = pump;
        displays.add(display);
        this.sugarSensor = sugarSensor;
        this.lastMeasurement = this.sugarSensor.getSugarInBlood();
        this.checkSugarStatus();
        this.checkInsulinStatus();
    }

    // testingmode
    public Controller(Pump pump, Display display, SugarSensor sugarSensor, InputHandler inputHandler){
        this(pump, display, sugarSensor);
        this.inputHandler = inputHandler;
    }

    public void addDisplay(){
        displays.add(new Display());
    }

    // control iteration
    public void play(){
        for (Display display : displays) {
            display.printData(lastMeasurement, remainingInsulin, sugarState, insulinState);
        }
        if (inputHandler != null) {
            inputHandler.processInput();
        }
        updateSugarMeasurements();
        checkSugarStatus();
        if(sugarState != SugarStates.LOWSUGAR && sugarState != SugarStates.VERYLOWSUGAR) {
            regulateSugar();
        }
        checkInsulinStatus();
    }

    private void updateSugarMeasurements() {
        int oldMeasurement = lastMeasurement;
        lastMeasurement = sugarSensor.getSugarInBlood();
        increment = lastMeasurement - oldMeasurement;
    }

    private void checkSugarStatus() {
        if(this.lastMeasurement < veryLowSugarLevel){
            sugarState = SugarStates.VERYLOWSUGAR;
        }
        else if (this.lastMeasurement < lowerSugarBound){
            sugarState = SugarStates.LOWSUGAR;
        }
        else if (this.lastMeasurement > veryHighSugarLevel){
            sugarState = SugarStates.VERYHIGHSUGAR;
        }
        else if (this.lastMeasurement > upperSugarBound){
            sugarState = SugarStates.HIGHSUGAR;
        }
        else{
            sugarState = SugarStates.GOOD;
        }
        this.sendIncrementInfo();
    }

    private void sendIncrementInfo() {
        if(increment < 0){
            for(Display display : displays){
                display.addInfo("Sugar is lowering");
            }
        }
        else if(increment > 0){
            for(Display display : displays){
                display.addInfo("Sugar is rising");
            }
        }
        else{
            for(Display display : displays){
                display.addInfo("Sugar is stable");
            }
        }
    }

    private void regulateSugar(){
        int littleAddition = 1;
        int hugeAddition = 10;

        int quantity = max(0, increment);
        if (sugarState == SugarStates.HIGHSUGAR) {
            quantity += littleAddition;
        }
        else if (sugarState == SugarStates.VERYHIGHSUGAR && increment >= 0){
            quantity += hugeAddition;
        }
        insulinInjection(quantity);
    }

    private void checkInsulinStatus() {
        remainingInsulin = pump.getAvailableInsulin();
        if(remainingInsulin == 0){
            insulinState = InsulinStates.EMPTY;
        }
        else if(remainingInsulin < lowerInsulinBound){
            insulinState = InsulinStates.LOWRESERVE;
        }
        else {
            insulinState = InsulinStates.GOOD;
        }
    }

    private void insulinInjection(int quantity) {
        try {
            pump.injectInsulin(quantity);
        }
        catch (InsulinAvailabilityException e){
            for (Display display : displays){
                display.addInfo("You need to fill the reservoir by almost "
                        + e.getRequiredAmount() +
                        " units to stop rising sugar level");
            }
        }
    }

    public static int getLowerSugarBound() {
        return lowerSugarBound;
    }

    public static int getUpperSugarBound() {
        return upperSugarBound;
    }
}

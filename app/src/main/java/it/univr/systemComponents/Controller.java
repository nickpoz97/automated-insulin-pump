package it.univr.systemComponents;

import it.univr.exceptions.InsulineAvailabilityException;
import it.univr.states.InsulinStates;
import it.univr.states.SugarStates;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class Controller {
    private static final int lowerSugarBound = 80;
    private static final int upperSugarBound = 180;
    private static final int lowerInsulinBound = 20;
    private static final int veryHighSugarLevel = 200;
    private static final int veryLowSugarLevel = 20;

    private final Pump pump;
    private final List<Display> displays = new ArrayList<>(2);
    private final SugarSensor sugarSensor;

    private SugarStates sugarState;
    private InsulinStates insulinState;

    private int remainingInsulin;
    private int oldMeasurement;
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
        updateSugarMeasurement();
        checkSugarStatus();
        if(sugarState != SugarStates.LOWSUGAR && sugarState != SugarStates.VERYLOWSUGAR) {
            regulateSugar();
        }
        checkInsulinStatus();
    }

    private void checkSugarStatus() {
        if(this.lastMeasurement < veryLowSugarLevel){
            sugarState = SugarStates.VERYLOWSUGAR;
        }
        else if (this.lastMeasurement >= veryLowSugarLevel && this.lastMeasurement < lowerSugarBound){
            sugarState = SugarStates.LOWSUGAR;
        }
        else if (this.lastMeasurement > upperSugarBound && this.lastMeasurement <= veryHighSugarLevel){
            sugarState = SugarStates.HIGHSUGAR;
        }
        else if (this.lastMeasurement > veryHighSugarLevel){
            sugarState = SugarStates.VERYHIGHSUGAR;
        }
        else{
            sugarState = SugarStates.GOOD;
        }
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

    private void regulateSugar(){
        increment = lastMeasurement - oldMeasurement;
        this.sendIncrementInfo(increment);
        int littleAddition = 2;
        int hugeAddition = 10;

        int quantity = max(0, increment);
        if (sugarState == SugarStates.HIGHSUGAR) {
            quantity += littleAddition;
        }
        else if (sugarState == SugarStates.VERYHIGHSUGAR && increment >= 0){
            quantity += hugeAddition;
        }
        insulineInjection(quantity);
    }

    private void sendIncrementInfo(int increment) {
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

    private void insulineInjection(int quantity) {
        try {
            pump.injectInsulin(quantity);
        }
        catch (InsulineAvailabilityException e){
            for (Display display : displays){
                display.addInfo("You need to fill the reservoir by " + e.getRequiredAmount() + 20 + " units");
            }
        }
    }

    private void updateSugarMeasurement() {
        oldMeasurement = lastMeasurement;
        lastMeasurement = sugarSensor.getSugarInBlood();
        increment = lastMeasurement - oldMeasurement;
    }

    public static int getLowerSugarBound() {
        return lowerSugarBound;
    }

    public static int getUpperSugarBound() {
        return upperSugarBound;
    }
}

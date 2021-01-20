package it.univr.systemComponents;

import it.univr.exceptions.InsulineAvailabilityException;
import it.univr.exceptions.LethalSugarValuesException;
import it.univr.states.InsulinStates;
import it.univr.states.SugarStates;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static final int lowerSugarBound = 80;
    private static final int upperSugarBound = 180;
    private static final int lowerInsulinBound = 10;

    private final Pump pump;
    private final List<Display> displays = new ArrayList<>(2);
    private final SugarSensor sugarSensor;

    private SugarStates sugarState;
    private InsulinStates insulinState;

    private int remainingInsulin;
    private int oldMeasurement;
    private int lastMeasurement;

    private InputHandler inputHandler = null;

    public Controller(Pump pump, Display display, SugarSensor sugarSensor) {
        this.pump = pump;
        displays.add(display);
        this.sugarSensor = sugarSensor;
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
        updateSugarMeasurement();
        check();

        for(Display display : displays) {
            display.printData(lastMeasurement, remainingInsulin, sugarState, insulinState);
        }
        if(inputHandler != null){
            inputHandler.processInput();
        }
        regulateSugar();
    }

    private void check(){
        try {
            checkSugarStatus();
            checkInsulinStatus();
        }
        catch(LethalSugarValuesException e){
            for(Display d : displays){
                d.printError("# Lethal sugar level reached #");
                System.exit(1);
            }
        }
    }

    private void checkSugarStatus() {
        if (this.lastMeasurement < lowerSugarBound){
            sugarState = SugarStates.LOWSUGAR;
        }
        else if (this.lastMeasurement > upperSugarBound){
            sugarState = SugarStates.HIGHSUGAR;
        }
        else{
            sugarState = SugarStates.GOOD;
        }
    }

    private void checkInsulinStatus() {
        remainingInsulin = pump.getAvailableInsulin();
        if(remainingInsulin < lowerInsulinBound){
            insulinState = InsulinStates.LOWRESERVE;
        }
        else {
            insulinState = InsulinStates.GOOD;
        }
    }

    private void regulateSugar() {
        int increment = lastMeasurement - oldMeasurement;
        System.out.println("increment: " + increment);

        try {
            if(increment > 1){ // sugar raising quickly
                pump.injectInsulin(increment);
            }
            else if(sugarState == SugarStates.HIGHSUGAR && increment >= 0){
                pump.injectInsulin(1);
            }
        }
        catch (InsulineAvailabilityException e){
            for(Display display : displays) {
                display.printError("# Not Enough insulin, fill the reservoir #");
            }
        }
    }

    private void updateSugarMeasurement() {
        this.oldMeasurement = sugarSensor.getSugarInBlood();
        this.lastMeasurement = sugarSensor.getSugarInBlood();
    }

    public static int getLowerSugarBound() {
        return lowerSugarBound;
    }

    public static int getUpperSugarBound() {
        return upperSugarBound;
    }
}

package it.univr.systemComponents;

import it.univr.exceptions.InsulinAvailabilityException;
import it.univr.states.InsulinStates;
import it.univr.states.SugarStates;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class Controller {
    // bounds
    private static final int LOWER_SUGAR_BOUND = 60;
    private static final int UPPER_SUGAR_BOUND = 100;
    private static final int HYPERGLYCEMIA_BOUND = 130;
    private static final int HYPOGLYCEMIA_BOUND = 20;
    private static final int LOWER_INSULIN_BOUND = 60;

    // constants used to lower sugar level
    private static final int LITTLE_ADDITION = 1;
    private static final int HUGE_ADDITION = 10;

    // sensor interfaces
    private final Pump pump;
    private final List<Display> displays = new ArrayList<>(2);
    private final SugarSensor sugarSensor;

    // status
    private int remainingInsulin;
    private int lastMeasurement;
    private int increment;
    private SugarStates sugarState;
    private InsulinStates insulinState;

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
        if(sugarState != SugarStates.LOW_SUGAR && sugarState != SugarStates.VERY_LOW_SUGAR) {
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
        if(this.lastMeasurement < HYPOGLYCEMIA_BOUND){
            sugarState = SugarStates.VERY_LOW_SUGAR;
        }
        else if (this.lastMeasurement < LOWER_SUGAR_BOUND){
            sugarState = SugarStates.LOW_SUGAR;
        }
        else if (this.lastMeasurement > HYPERGLYCEMIA_BOUND){
            sugarState = SugarStates.VERY_HIGH_SUGAR;
        }
        else if (this.lastMeasurement > UPPER_SUGAR_BOUND){
            sugarState = SugarStates.HIGH_SUGAR;
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
        int quantity = max(0, increment);
        if (sugarState == SugarStates.HIGH_SUGAR) {
            quantity += LITTLE_ADDITION;
        }
        else if (sugarState == SugarStates.VERY_HIGH_SUGAR && increment >= 0){
            quantity += HUGE_ADDITION;
        }
        insulinInjection(quantity);
    }

    private void checkInsulinStatus() {
        remainingInsulin = pump.getAvailableInsulin();
        if(remainingInsulin == 0){
            insulinState = InsulinStates.EMPTY;
        }
        else if(remainingInsulin < LOWER_INSULIN_BOUND){
            insulinState = InsulinStates.LOW_RESERVE;
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
        return LOWER_SUGAR_BOUND;
    }

    public static int getUpperSugarBound() {
        return UPPER_SUGAR_BOUND;
    }

    public static int getLowerInsulinBound(){ return LOWER_INSULIN_BOUND; }

    public int getIncrement() {
        return increment;
    }

    public SugarStates getSugarState() {
        return sugarState;
    }

    public InsulinStates getInsulinState() {
        return insulinState;
    }

    public int getDisplayNumber(){
        return displays.size();
    }
}

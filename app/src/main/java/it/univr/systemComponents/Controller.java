package it.univr.SystemComponents;

import it.univr.exceptions.InsulineAvailabilityException;

public class Controller {
    private static final int lowerSugarBound = 80;
    private static final int upperSugarBound = 100;
    private static final int lowerInsulinBound = 10;

    private final Pump pump;
    private final Display display;
    private final SugarSensor sugarSensor;
    //private final boolean interactiveMode;

    private SugarStates sugarState = SugarStates.GOOD;
    private InsulinStates insulinState = InsulinStates.GOOD;

    private int remainingInsulin;
    private int[] sugarMeasurements = new int[3];

    // testing flag
    private final boolean isInteractive;

    public Controller(Pump pump, Display display, SugarSensor sugarSensor, boolean isInteractive) {
        this.pump = pump;
        this.display = display;
        this.sugarSensor = sugarSensor;
        this.isInteractive = isInteractive;
    }

    // loop for Controller
    public void play(){
        // initialize measurements (position 0 is for most recent check)
        for(int i = sugarMeasurements.length-1; i >= 0 ; i++){
            sugarMeasurements[i] = sugarSensor.getSugarInBlood();
        }

        while(true){
            check();
            display.printData(sugarMeasurements[0], remainingInsulin, sugarState, insulinState);
            if(this.isInteractive) {
                display.inputHandler();
            }
            regulateSugar();
            updateSugarMeasurement();
        }
    }

    private void check(){
        checkSugarStatus();
        checkInsulinStatus();
    }

    private void checkSugarStatus() {
        int lastSugarMeasurement = sugarMeasurements[0];
        if (lastSugarMeasurement < lowerSugarBound){
            sugarState = SugarStates.LOWSUGAR;
        }
        else if (lastSugarMeasurement > upperSugarBound){
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
        int ir = calculateIncrementRate();

        try {
            if(sugarState == SugarStates.HIGHSUGAR){
                pump.injectInsulin(ir + 1);
            }
            else if(ir > 1){ // sugar raising quickly
                pump.injectInsulin(ir);
            }
        }
        catch (InsulineAvailabilityException e){
            display.printError("#Not Enough insulin, fill the reservoir#");
        }
    }

    private int calculateIncrementRate() {
        int d1 = sugarMeasurements[0] - sugarMeasurements[1];
        int d2 = sugarMeasurements[1] - sugarMeasurements[2];

        return d2-d1;
    }

    private void updateSugarMeasurement() {
        int nMeasurements = sugarMeasurements.length;
        // circular shifting
        for(int i = 0; i < nMeasurements ; i++){
            sugarMeasurements[(i+1)%nMeasurements] = sugarMeasurements[i];
        }
        // oldest measurement overwritten with newest
        sugarMeasurements[0] = sugarSensor.getSugarInBlood();
    }
}

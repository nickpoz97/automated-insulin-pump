package it.univr.systemComponents;

import it.univr.exceptions.InsulineAvailabilityException;
import it.univr.states.InsulinStates;
import it.univr.states.SugarStates;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static final int lowerSugarBound = 80;
    private static final int upperSugarBound = 100;
    private static final int lowerInsulinBound = 10;

    private final Pump pump;
    private final List<Display> displays = new ArrayList<>(2);
    private final SugarSensor sugarSensor;

    private SugarStates sugarState = SugarStates.GOOD;
    private InsulinStates insulinState = InsulinStates.GOOD;

    private int remainingInsulin;
    private int[] sugarMeasurements = new int[3];

    public Controller(Pump pump, Display display, SugarSensor sugarSensor) {
        this.pump = pump;
        displays.add(display);
        this.sugarSensor = sugarSensor;
    }

    public void addDisplay(){
        displays.add(new Display());
    }

    // loop for Controller
    public void play(){
        // initialize measurements (position 0 is for most recent check)
        for(int i = sugarMeasurements.length-1; i >= 0 ; i++){
            sugarMeasurements[i] = sugarSensor.getSugarInBlood();
        }

        while(true){
            check();
            for(Display display : displays) {
                display.printData(sugarMeasurements[0], remainingInsulin, sugarState, insulinState);
            }
            // only first display can handle input
            displays.get(0).inputHandler();
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
            for(Display display : displays) {
                display.printError("# Not Enough insulin, fill the reservoir #");
            }
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

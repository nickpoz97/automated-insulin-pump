package it.univr;

public class Controller {
    private static final int lowerSugarBound = 80;
    private static final int upperSugarBound = 100;
    private static final int lowerInsulinBound = 10;

    private final Pump pump;
    private final Display display;
    private final SugarSensor sugarSensor;
    private SugarStates sugarState = SugarStates.GOOD;
    private InsulinStates insulinState = InsulinStates.GOOD;

    private int remainingInsulin;
    private int sugarLevel;

    public Controller(Pump pump, Display display, SugarSensor sugarSensor) {
        this.pump = pump;
        this.display = display;
        this.sugarSensor = sugarSensor;
    }

    // loop for Controller
    public void boot(){
        while(true){
            check();
            display.printData(sugarLevel, remainingInsulin);
        }
    }

    private void check(){
        checkSugar();
        checkInsulin();
    }

    private void checkSugar() {
        sugarLevel = sugarSensor.getSugarInBlood();
        if (sugarLevel < lowerSugarBound){
            sugarState = SugarStates.LOWSUGAR;
        }
        else if (sugarLevel > upperSugarBound){
            sugarState = SugarStates.HIGHSUGAR;
        }
        else{
            sugarState = SugarStates.GOOD;
        }
    }

    private void checkInsulin() {
        remainingInsulin = pump.getAvailableInsulin();
        if(remainingInsulin < lowerInsulinBound){
            insulinState = InsulinStates.LOWRESERVE;
        }
        else {
            insulinState = InsulinStates.GOOD;
        }
    }
}

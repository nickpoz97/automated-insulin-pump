package it.univr;

import it.univr.mocks.BloodData;
import it.univr.mocks.InteractiveBloodData;
import it.univr.systemComponents.*;

public class AutomatedInsulinPump {
    private Controller controller;

    public AutomatedInsulinPump(int sugarLevel, int incrementValue, int incrementRate, int insulinAmount, boolean testingMode){
        BloodData bloodData = null;
        if(testingMode){
            bloodData = new InteractiveBloodData(sugarLevel, incrementValue, incrementRate);
        }
        InsulineReservoir insulineReservoir = new InsulineReservoir(insulinAmount);
        SugarSensor sugarSensor = new SugarSensor(bloodData);
        Pump pump = new Pump(insulineReservoir, bloodData);
        Display display = new Display(bloodData, insulineReservoir);
        Controller controller = new Controller(pump, display, sugarSensor, bloodData.isInterative());
    }

    public void run(){
        controller.play();
    }
}

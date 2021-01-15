package it.univr;

import it.univr.bloodModels.BloodModel;
import it.univr.bloodModels.InteractiveBloodModel;
import it.univr.systemComponents.*;

public class AutomatedInsulinPump {
    private Controller controller;

    public AutomatedInsulinPump(int sugarLevel, int incrementValue, int incrementRate, int insulinAmount, boolean testingMode){
        BloodModel bloodModel = null;
        if(testingMode){
            bloodModel = new InteractiveBloodModel(sugarLevel, incrementValue, incrementRate);
        }
        InsulineReservoir insulineReservoir = new InsulineReservoir(insulinAmount);
        SugarSensor sugarSensor = new SugarSensor(bloodModel);
        Pump pump = new Pump(insulineReservoir, bloodModel);
        Display display = new Display(bloodModel, insulineReservoir);
        Controller controller = new Controller(pump, display, sugarSensor, bloodModel.isInterative());
    }

    public void run(){
        controller.play();
    }
}

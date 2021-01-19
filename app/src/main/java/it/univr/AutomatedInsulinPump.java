package it.univr;

import it.univr.bloodModels.BloodModel;
import it.univr.bloodModels.InteractiveBloodModel;
import it.univr.systemComponents.*;
import it.univr.systemComponents.Controller;
import it.univr.systemComponents.Display;

public class AutomatedInsulinPump {
    private Controller controller;
    private BloodModel bloodModel;
    private InsulineReservoir insulineReservoir;
    private boolean testingMode;

    public AutomatedInsulinPump(int sugarLevel, int incrementValue, int incrementRate, int insulinAmount, boolean testingMode){
        this.testingMode = testingMode;
        this.insulineReservoir = new InsulineReservoir(insulinAmount);
        instantiateBloodModel(sugarLevel, incrementValue, incrementRate);
        SugarSensor sugarSensor = new SugarSensor(bloodModel);
        Pump pump = new Pump(insulineReservoir, bloodModel);
        Display display = new Display();
        this.controller = new Controller(pump, display, sugarSensor);
    }

    private void instantiateBloodModel(int sugarLevel, int incrementValue, int incrementRate) {
        if(testingMode)
            this.bloodModel = new InteractiveBloodModel(sugarLevel, incrementRate);
    }

    private void instantiateController(Pump pump, Display display, SugarSensor sugarSensor){
        if(testingMode){
            InputHandler inputHandler = new InputHandler(bloodModel, insulineReservoir);
            controller = new Controller(pump,display,sugarSensor,inputHandler);
        }
    }

    public void run(){
        controller.play();
    }
}

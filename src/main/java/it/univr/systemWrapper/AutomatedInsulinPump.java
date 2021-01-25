package it.univr.systemWrapper;

import it.univr.bloodModels.BloodModel;
import it.univr.bloodModels.InteractiveBloodModel;
import it.univr.systemComponents.*;
import it.univr.systemComponents.Controller;
import it.univr.systemComponents.Display;

public class AutomatedInsulinPump {
    private Controller controller;
    private BloodModel bloodModel;
    private final InsulinReservoir insulinReservoir;
    private final boolean interactiveMode;

    public AutomatedInsulinPump(int sugarLevel, int incrementRate, int insulinAmount, boolean interactiveMode){
        this.interactiveMode = interactiveMode;
        this.insulinReservoir = new InsulinReservoir(insulinAmount);
        instantiateBloodModel(sugarLevel, incrementRate);
        SugarSensor sugarSensor = new SugarSensor(bloodModel);
        Pump pump = new Pump(insulinReservoir, bloodModel);
        Display display = new Display();
        instantiateController(pump, display, sugarSensor);
    }

    private void instantiateBloodModel(int sugarLevel, int incrementRate) {
        if(interactiveMode) {
            this.bloodModel = new InteractiveBloodModel(sugarLevel, incrementRate);
        }
    }

    private void instantiateController(Pump pump, Display display, SugarSensor sugarSensor){
        if(interactiveMode){
            InputHandler inputHandler = new InputHandler(bloodModel, insulinReservoir);
            this.controller = new Controller(pump,display,sugarSensor,inputHandler);
        }
        else{
            this.controller = new Controller(pump,display,sugarSensor);
        }
    }

    public void run(){
        boolean continueLoop = true;

        while(continueLoop) {
            continueLoop = this.controller.play();
        }
    }

    public Controller getController() {
        return controller;
    }
}

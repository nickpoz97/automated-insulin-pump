package it.univr;

import it.univr.bloodModels.BloodModel;
import it.univr.bloodModels.InteractiveBloodModel;
import it.univr.systemComponents.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ControllerTests {
    private Controller controller;
    private BloodModel bloodModel;

    @Before
    public void instantiation(){
        InsulineReservoir insulineReservoir = new InsulineReservoir();
        this.bloodModel = new InteractiveBloodModel();
        SugarSensor sugarSensor = new SugarSensor(bloodModel);
        Pump pump = new Pump(insulineReservoir, bloodModel);
        Display display = new Display();
        this.controller = new Controller(pump, display, sugarSensor);
    }

    @Test
    public void test(){
        this.bloodModel.addSugar(2);
        this.controller.play();
        assertEquals(0, bloodModel.getIncrementRate());
    }
}

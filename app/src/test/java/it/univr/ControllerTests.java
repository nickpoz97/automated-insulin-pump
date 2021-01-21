package it.univr;

import it.univr.bloodModels.BloodModel;
import it.univr.bloodModels.InteractiveBloodModel;
import it.univr.systemComponents.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ControllerTests {
    private Controller controller;
    private BloodModel bloodModel;

    @Before
    public void instantiate(){
        InsulineReservoir insulineReservoir = new InsulineReservoir();
        this.bloodModel = new InteractiveBloodModel();
        SugarSensor sugarSensor = new SugarSensor(bloodModel);
        Pump pump = new Pump(insulineReservoir, bloodModel);
        Display display = new Display();
        this.controller = new Controller(pump, display, sugarSensor);
    }

    @Test
    public void testFunctioning(){
        int startingSugar = bloodModel.getBaseSugarLevel();
        this.bloodModel.addSugar(2);
        this.controller.play();

        for(int i = 0 ; i < 1000 ; i++){
            assertEquals(0, bloodModel.getIncrementRate());
            assertEquals(startingSugar + 4, bloodModel.getSugarLevel());
        }

        this.bloodModel.addSugar(30);
        this.controller.play();
        assertEquals(0, bloodModel.getIncrementRate());

        for(int i = 0 ; i < 10 ; i++){
            this.controller.play();
            assertEquals(-1, bloodModel.getIncrementRate());
        }

        assertTrue(bloodModel.getSugarLevel() < Controller.getUpperSugarBound());
        bloodModel.addSugar(1);
        this.controller.play();
        assertEquals(0, this.bloodModel.getIncrementRate());
        assertTrue(this.bloodModel.getSugarLevel() > Controller.getLowerSugarBound());
    }
}

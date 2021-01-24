package it.univr;

import it.univr.bloodModels.BloodModel;
import it.univr.bloodModels.InteractiveBloodModel;
import it.univr.systemComponents.SugarSensor;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SugarSensorTests {
    private BloodModel bloodModel;
    private SugarSensor sugarSensor;

    @Before
    public void initialize(){
        this.bloodModel = new InteractiveBloodModel();
        sugarSensor = new SugarSensor(bloodModel);
        bloodModel.addSugar(5);
    }

    @Test
    public void testGetSugarInBlood(){
        assertEquals(bloodModel.getBaseSugarLevel(), sugarSensor.getSugarInBlood());
        assertEquals(bloodModel.getBaseSugarLevel() + 5, sugarSensor.getSugarInBlood());
    }

}

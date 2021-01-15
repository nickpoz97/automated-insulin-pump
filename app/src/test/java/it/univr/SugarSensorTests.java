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
    private static int initialSugarValue = (BloodModel.getMaxSugar()+BloodModel.getMinSugar())/2;

    @Before
    public void initializeBloodModel(){
        bloodModel = new InteractiveBloodModel(initialSugarValue, 4, 5);
        sugarSensor = new SugarSensor(bloodModel);
    }

    @Test
    public void testGetSugarInBlood(){
        assertEquals(initialSugarValue, sugarSensor.getSugarInBlood());
    }

}

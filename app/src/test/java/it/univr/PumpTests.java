package it.univr;

import it.univr.bloodModels.BloodModel;
import it.univr.bloodModels.InteractiveBloodModel;
import it.univr.exceptions.InsulineAvailabilityException;
import it.univr.systemComponents.InsulineReservoir;
import it.univr.systemComponents.Pump;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PumpTests {
    private InsulineReservoir insulineReservoir;
    private BloodModel bloodModel;
    private Pump pump;
    private static int initialSugarLevel = (BloodModel.getMaxSugar() + BloodModel.getMinSugar())/2;


    @Before
    public void initialization(){
        this.insulineReservoir = new InsulineReservoir(4);
        bloodModel = new InteractiveBloodModel(initialSugarLevel, 4);
        this.pump = new Pump(this.insulineReservoir, this.bloodModel);
    }

    @Test
    public void testInjectInsulin(){
        try {
            pump.injectInsulin(4);
            assertEquals(0, bloodModel.getIncrementRate());
            pump.injectInsulin(20);
            fail();
        }
        catch (InsulineAvailabilityException e){
            assertTrue(true);
        }
    }
}

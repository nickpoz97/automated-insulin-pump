package it.univr;

import it.univr.bloodModels.BloodModel;
import it.univr.bloodModels.InteractiveBloodModel;
import it.univr.exceptions.InsulinAvailabilityException;
import it.univr.systemComponents.InsulinReservoir;
import it.univr.systemComponents.Pump;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PumpTests {
    private InsulinReservoir insulinReservoir;
    private BloodModel bloodModel;
    private Pump pump;


    @Before
    public void initialization(){
        this.insulinReservoir = new InsulinReservoir(4);
        bloodModel = new InteractiveBloodModel();
        this.pump = new Pump(this.insulinReservoir, this.bloodModel);
    }

    @Test
    public void testInjectInsulin(){
        try {
            pump.injectInsulin(4);
            assertEquals(-4, bloodModel.getIncrementRate());
            pump.injectInsulin(20);
            fail();
        }
        catch (InsulinAvailabilityException e){
            assertTrue(true);
        }
    }
}

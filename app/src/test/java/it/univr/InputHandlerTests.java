package it.univr;

import it.univr.bloodModels.BloodModel;
import it.univr.bloodModels.InteractiveBloodModel;
import it.univr.systemComponents.InputHandler;
import it.univr.systemComponents.InsulineReservoir;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class InputHandlerTests {
    private BloodModel bloodModel;
    private InsulineReservoir insulineReservoir;
    private InputHandler inputHandler;

    @Before
    public void initilization(){
        bloodModel = new InteractiveBloodModel();
        insulineReservoir = new InsulineReservoir(100);
        inputHandler = new InputHandler(bloodModel, insulineReservoir);
    }

    @Test
    public void addInsulin() {
        InputStream in = new ByteArrayInputStream("i\n100\nc".getBytes());
        assertEquals(200, insulineReservoir.getAmount());
    }
}

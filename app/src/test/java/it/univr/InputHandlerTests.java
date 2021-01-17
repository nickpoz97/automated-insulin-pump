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
    public void testAddInsulin() {
        int formerAmount = insulineReservoir.getAmount();
        // +100 insulin
        this.setStdInput("i\n100\nc\n");
        inputHandler.processInput();
        int actualAmount = Math.min(formerAmount + 100, InsulineReservoir.getCapacity()) ;
        assertEquals(actualAmount, insulineReservoir.getAmount());
    }

    @Test
    public void testAddSugar(){
        int formerAmount = bloodModel.getIncrementRate();
        // +4 sugar
        this.setStdInput("s\n4\nc\n");
        inputHandler.processInput();
        int actualAmount = formerAmount+4;
        assertEquals(actualAmount, bloodModel.getIncrementRate());
    }

    @Test
    public void testAddBoth(){
        int formerBlood = bloodModel.getIncrementRate();
        int formerInsulin = insulineReservoir.getAmount();
        // +8 sugar, +30 insulin, '0' sugar, '0' insulin
        this.setStdInput("s\n8\ni\n30\ns\n-4\ni\n-6\nc\n");
        inputHandler.processInput();
        int actualBlood = formerBlood+8;
        int actualInsulin = Math.min(formerInsulin + 30, InsulineReservoir.getCapacity()) ;
        assertEquals(actualBlood, bloodModel.getIncrementRate());
        assertEquals(actualInsulin, insulineReservoir.getAmount());
    }

    private void setStdInput(String inputString){
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);
    }
}

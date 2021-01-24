package it.univr;

import it.univr.bloodModels.BloodModel;
import it.univr.bloodModels.InteractiveBloodModel;
import it.univr.systemComponents.InputHandler;
import it.univr.systemComponents.InsulinReservoir;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class InputHandlerTests {
    private BloodModel bloodModel;
    private InsulinReservoir insulinReservoir;
    private InputHandler inputHandler;

    @Before
    public void initilization(){
        bloodModel = new InteractiveBloodModel();
        insulinReservoir = new InsulinReservoir();
        inputHandler = new InputHandler(bloodModel, insulinReservoir);
        // delete output
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
        // backup input stream
    }

    //@Test
    public void testAddInsulin() {
        int formerAmount = insulinReservoir.getAmount();
        // +100 insulin
        this.setStdInput("i\n100\nc\n");
        inputHandler.processInput();
        int actualAmount = Math.min(formerAmount + 100, InsulinReservoir.getCapacity()) ;
        assertEquals(actualAmount, insulinReservoir.getAmount());
    }

    //@Test
    public void testAddSugar(){
        int formerAmount = bloodModel.getIncrementRate();
        // +4 sugar
        this.setStdInput("s\n4\nc\n");
        inputHandler.processInput();
        int actualAmount = formerAmount+4;
        assertEquals(actualAmount, bloodModel.getIncrementRate());
    }

    //@Test
    public void testAddBoth(){
        int formerBlood = bloodModel.getIncrementRate();
        int formerInsulin = insulinReservoir.getAmount();
        // +8 sugar, +30 insulin, '0' sugar, '0' insulin
        this.setStdInput("s\n8\ni\n30\ns\n-4\ni\n-6\nc\n");
        inputHandler.processInput();
        int actualBlood = formerBlood+8;
        int actualInsulin = Math.min(formerInsulin + 30, InsulinReservoir.getCapacity()) ;
        assertEquals(actualBlood, bloodModel.getIncrementRate());
        assertEquals(actualInsulin, insulinReservoir.getAmount());
    }

    private void setStdInput(String inputString){
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);
    }
}

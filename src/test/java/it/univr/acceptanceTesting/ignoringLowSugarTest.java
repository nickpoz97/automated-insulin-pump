package it.univr.acceptanceTesting;

import it.univr.states.SugarStates;
import it.univr.systemComponents.Controller;
import it.univr.systemComponents.InputHandler;
import it.univr.systemComponents.InsulinReservoir;
import it.univr.systemWrapper.AutomatedInsulinPump;
import org.junit.Before;
import org.junit.Test;

import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class ignoringLowSugarTest {
    private AutomatedInsulinPump automatedInsulinPump;

    @Before
    public void initialize(){
        int sugarLevel = Controller.getLowerSugarBound() -10;
        int incrementRate = -5;
        int insulinLevel = InsulinReservoir.getCapacity();
        automatedInsulinPump = new AutomatedInsulinPump(sugarLevel,incrementRate,insulinLevel,true);
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
    }

    @Test
    public void ignoringBehaviorTest(){
        InputHandler.updateInputStream("e\n");
        SugarStates sugarState = automatedInsulinPump.getController().getSugarState();
        assertEquals(SugarStates.LOW_SUGAR, sugarState);

        automatedInsulinPump.run();
        sugarState = automatedInsulinPump.getController().getSugarState();
        // ignoring it one time is not dangerous
        assertEquals(SugarStates.LOW_SUGAR, sugarState);

        String inputString = "";
        for (int i = 0 ; i < 15 ; i++){
            inputString += "c\n";
        }
        inputString += "e\n";
        InputHandler.updateInputStream(inputString);

        automatedInsulinPump.run();
        sugarState = automatedInsulinPump.getController().getSugarState();
        // ignoring it many times is dangerous
        assertEquals(SugarStates.VERY_LOW_SUGAR, sugarState);
    }
}

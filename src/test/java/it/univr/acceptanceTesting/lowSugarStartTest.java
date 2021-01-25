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
import static org.junit.Assert.assertTrue;

public class lowSugarStartTest {
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
    public void normalFlowTest(){
        // + 8 sugar
        InputHandler.updateInputStream("s\n8\ne\n");

        SugarStates sugarState = automatedInsulinPump.getController().getSugarState();
        assertEquals(SugarStates.LOW_SUGAR, sugarState);

        automatedInsulinPump.run();
        int increment = automatedInsulinPump.getController().getSugarIncrement();

        assertTrue(increment > 0);
        assertTrue(increment < 10);
    }

    @Test
    public void badSituationTest1(){
        // + 50 sugar (exit instantly because pump would stop the increment)
        InputHandler.updateInputStream("s\n50\ne\n");

        SugarStates sugarState = automatedInsulinPump.getController().getSugarState();
        assertEquals(SugarStates.LOW_SUGAR, sugarState);

        automatedInsulinPump.run();
        int increment = automatedInsulinPump.getController().getSugarIncrement();
        assertTrue(increment > 30);
    }

    @Test
    public void badSituationTest2(){
        // + 50 sugar (exit instantly because pump would stop the increment)
        InputHandler.updateInputStream("s\n4\ne\n");

        SugarStates sugarState = automatedInsulinPump.getController().getSugarState();
        assertEquals(SugarStates.LOW_SUGAR, sugarState);

        automatedInsulinPump.run();
        int increment = automatedInsulinPump.getController().getSugarIncrement();
        assertTrue(increment < 0);
    }
}

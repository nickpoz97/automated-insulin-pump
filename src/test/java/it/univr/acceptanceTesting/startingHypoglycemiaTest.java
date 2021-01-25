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

public class startingHypoglycemiaTest {
    private AutomatedInsulinPump automatedInsulinPump;

    @Before
    public void initialize(){
        int sugarLevel = Controller.getHypoglycemiaBound() - 5;
        int incrementRate = -5;
        int insulinLevel = InsulinReservoir.getCapacity();
        automatedInsulinPump = new AutomatedInsulinPump(sugarLevel,incrementRate,insulinLevel,true);
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
    }

    @Test
    public void normalFlow(){
        // + 20 sugar
        InputHandler.updateInputStream("s\n20\nc\ne\n");

        SugarStates sugarState = automatedInsulinPump.getController().getSugarState();
        assertEquals(SugarStates.VERY_LOW_SUGAR, sugarState);

        automatedInsulinPump.run();
        int increment = automatedInsulinPump.getController().getIncrement();
        assertTrue(increment > 10);
    }

    @Test
    public void badSituation(){
        // +3 sugar
        InputHandler.updateInputStream("s\n3\nc\ne\n");

        SugarStates sugarState = automatedInsulinPump.getController().getSugarState();
        assertEquals(SugarStates.VERY_LOW_SUGAR, sugarState);
        automatedInsulinPump.run();
        int increment = automatedInsulinPump.getController().getIncrement();
        assertTrue(increment <= 0);
    }
}

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

public class startHyperglycemiaTest {
    private AutomatedInsulinPump automatedInsulinPump;

    @Before
    public void initialize(){
        int sugarLevel = Controller.getHyperglycemiaBound() + 20;
        int incrementRate = 10;
        int insulinLevel = 10;
        automatedInsulinPump = new AutomatedInsulinPump(sugarLevel,incrementRate,insulinLevel,true);
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
    }

    @Test
    public void normalFlow(){
        // insulin refilled e some iterations
        InputHandler.updateInputStream("i\n500\nc\ne\n");

        SugarStates sugarState = automatedInsulinPump.getController().getSugarState();
        assertEquals(SugarStates.VERY_HIGH_SUGAR, sugarState);

        automatedInsulinPump.run();
        int increment = automatedInsulinPump.getController().getIncrement();

        assertTrue(increment <= -10);
    }

    @Test
    public void badSituation(){
        // +do nothing (insulin not sufficient)
        InputHandler.updateInputStream("c\ne\n");

        SugarStates sugarState = automatedInsulinPump.getController().getSugarState();
        assertEquals(SugarStates.VERY_HIGH_SUGAR, sugarState);
        automatedInsulinPump.run();
        int increment = automatedInsulinPump.getController().getIncrement();
        assertTrue(increment >= 0);

        // refill insulin
        InputHandler.updateInputStream("i\n500\nc\ne\n");
        automatedInsulinPump.run();
        increment = automatedInsulinPump.getController().getIncrement();
        assertTrue(increment < 0);
    }
}

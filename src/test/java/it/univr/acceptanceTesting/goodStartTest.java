package it.univr.acceptanceTesting;

import it.univr.states.InsulinStates;
import it.univr.states.SugarStates;
import it.univr.systemComponents.Controller;
import it.univr.systemComponents.InputHandler;
import it.univr.systemWrapper.AutomatedInsulinPump;
import org.junit.Before;
import org.junit.Test;

import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class goodStartTest {
    private AutomatedInsulinPump automatedInsulinPump;

    @Before
    public void initialize(){
        int sugarLevel = Controller.getUpperSugarBound() -40;
        int incrementRate = 0;
        int insulinLevel = Controller.getLowerInsulinBound() + 10;
        automatedInsulinPump = new AutomatedInsulinPump(sugarLevel,incrementRate,insulinLevel,true);
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
    }

    @Test
    public void normalFlowTest(){
        // +10 sugar
        InputHandler.updateInputStream("s\n10\ne\n");
        automatedInsulinPump.run();

        SugarStates sugarState = automatedInsulinPump.getController().getSugarState();
        InsulinStates insulinState = automatedInsulinPump.getController().getInsulinState();

        assertEquals(sugarState, SugarStates.GOOD);
        assertEquals(insulinState, InsulinStates.GOOD);
    }

    @Test
    public void badSituationTest(){
        // + 50 sugar
        InputHandler.updateInputStream("s\n50\ne\n");
        automatedInsulinPump.run();

        SugarStates sugarState = automatedInsulinPump.getController().getSugarState();
        InsulinStates insulinState = automatedInsulinPump.getController().getInsulinState();

        assertEquals(sugarState, SugarStates.HIGH_SUGAR);
        assertEquals(insulinState, InsulinStates.LOW_RESERVE);

        String inputString = "";
        for(int i = 0 ; i < 5 ; i++){
            inputString += "c\n";
        }
        inputString += "e\n";
        InputHandler.updateInputStream(inputString);

        automatedInsulinPump.run();
        sugarState = automatedInsulinPump.getController().getSugarState();
        insulinState = automatedInsulinPump.getController().getInsulinState();

        assertEquals(sugarState, SugarStates.GOOD);
        assertEquals(insulinState, InsulinStates.LOW_RESERVE);
    }


}

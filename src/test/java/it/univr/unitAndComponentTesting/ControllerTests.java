package it.univr.unitAndComponentTesting;

import it.univr.bloodModels.BloodModel;
import it.univr.bloodModels.InteractiveBloodModel;
import it.univr.states.InsulinStates;
import it.univr.states.SugarStates;
import it.univr.systemComponents.*;
import org.junit.Before;
import org.junit.Test;

import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class ControllerTests {
    private Controller controller;

    @Before
    public void initialize(){
        BloodModel bloodModel = new InteractiveBloodModel();
        InsulinReservoir insulinReservoir = new InsulinReservoir();
        InputHandler inputHandler = new InputHandler(bloodModel, insulinReservoir);
        Pump pump = new Pump(insulinReservoir, bloodModel);
        SugarSensor sugarSensor = new SugarSensor(bloodModel);
        Display display = new Display();
        controller = new Controller(pump, display, sugarSensor, inputHandler);
        // eliminate output
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
    }

    @Test
    public void addDisplay(){
        controller.addDisplay();
        controller.addDisplay();

        assertEquals(controller.getDisplayNumber(), 3);
    }

    @Test
    public void checkFunctioning(){
        assertEquals(InsulinStates.GOOD, controller.getInsulinState());
        assertEquals(SugarStates.GOOD, controller.getSugarState());

        // do nothing
        InputHandler.updateInputStream("c\n");
        controller.play();

        assertEquals(InsulinStates.GOOD, controller.getInsulinState());
        assertEquals(SugarStates.GOOD, controller.getSugarState());

        // + 40 sugar
        InputHandler.updateInputStream("s\n40\nc\n");
        controller.play();

        assertEquals(InsulinStates.GOOD, controller.getInsulinState());
        assertEquals(SugarStates.HIGH_SUGAR, controller.getSugarState());
        assertTrue(controller.getSugarIncrement() > 0);

        for(int i = 0 ; i < 10 ; i++){
            // each iter: +50 sugar
            InputHandler.updateInputStream("s\n50\nc\n");
            controller.play();
        }

        assertEquals(InsulinStates.EMPTY, controller.getInsulinState());
        assertEquals(SugarStates.VERY_HIGH_SUGAR, controller.getSugarState());

        // +300 insulin
        InputHandler.updateInputStream("i\n300\nc\n");
        controller.play();

        for(int i = 0 ; i < 10 ; i++){
            InputHandler.updateInputStream("c\n");
            controller.play();
        }

        assertTrue(controller.getSugarIncrement() < 0);
        assertNotSame(controller.getSugarState(), SugarStates.HIGH_SUGAR);
    }
}

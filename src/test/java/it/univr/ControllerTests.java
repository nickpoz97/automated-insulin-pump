package it.univr;

import it.univr.bloodModels.BloodModel;
import it.univr.bloodModels.InteractiveBloodModel;
import it.univr.states.InsulinStates;
import it.univr.states.SugarStates;
import it.univr.systemComponents.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class ControllerTests {
    private BloodModel bloodModel;
    private InsulinReservoir insulinReservoir;
    private Controller controller;
    private InputHandler inputHandler;

    @Before
    public void initialize(){
        bloodModel = new InteractiveBloodModel();
        insulinReservoir = new InsulinReservoir();
        inputHandler = new InputHandler(bloodModel, insulinReservoir);
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
        setInput("c\n");
        controller.play();

        assertEquals(InsulinStates.GOOD, controller.getInsulinState());
        assertEquals(SugarStates.GOOD, controller.getSugarState());

        // + 40 sugar
        setInput("s\n40\nc\n");
        controller.play();

        assertEquals(InsulinStates.GOOD, controller.getInsulinState());
        assertEquals(SugarStates.HIGH_SUGAR, controller.getSugarState());
        assertTrue(controller.getIncrement() > 0);

        for(int i = 0 ; i < 10 ; i++){
            // each iter: +50 sugar
            setInput("s\n50\nc\n");
            controller.play();
        }

        assertEquals(InsulinStates.EMPTY, controller.getInsulinState());
        assertEquals(SugarStates.VERY_HIGH_SUGAR, controller.getSugarState());

        // +300 insulin
        setInput("i\n300\nc\n");
        controller.play();

        for(int i = 0 ; i < 10 ; i++){
            setInput("c\n");
            controller.play();
        }

        assertTrue(controller.getIncrement() < 0);
        assertTrue(controller.getSugarState() != SugarStates.HIGH_SUGAR);
    }

    private void setInput(String inputString){
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(inputStream);
        inputHandler.updateInputStream();
    }
}

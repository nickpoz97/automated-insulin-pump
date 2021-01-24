package it.univr;

import it.univr.states.InsulinStates;
import it.univr.states.SugarStates;
import it.univr.systemComponents.Display;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Calendar;

public class DisplayTests {
    private Display display;
    private ByteArrayOutputStream output;

    @Before
    public void initialize(){
        display = new Display();
        output = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(output);
        System.setOut(ps);
    }

    @Test
    public void outputTest(){
        display.addInfo("testingInfo");
        display.printData(30, 0, SugarStates.LOW_SUGAR, InsulinStates.EMPTY);

        String expected = createOutputString(
                30,
                0,
                "Insulin: DANGER! Empty reservoir, fill it!" + '\n' +
                        "Sugar: WARNING! low sugar level" + '\n',
                "testingInfo"
                );

        assertEquals(expected.trim(), output.toString().trim());
    }

    private String createOutputString(int sugarLevel,
                                      int remainingInsulin,
                                      String expectedStatusMessage,
                                      String expectedInfos){
        Calendar timestamp = display.getTimestamp();
        timestamp.add(Calendar.MINUTE, -10);
        String timestampString = String.format("Date: %td/%tm/%tY %tT\n", timestamp, timestamp, timestamp, timestamp);
        String sugarLevelString = "Sugar level: " + sugarLevel + '\n';
        String insulinString = "Remaining insulin: " + remainingInsulin + '\n';

        return timestampString + sugarLevelString +
                insulinString + expectedStatusMessage + expectedInfos + '\n';
    }
}

package it.univr.unitAndComponentTesting;
import it.univr.systemComponents.InputHandler;
import it.univr.systemWrapper.AutomatedInsulinPump;
import org.junit.Test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class SystemTests {
    private static final Random random = new Random();

    @Test
    public void testSystem(){
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));

        String inputString = "";
        for(int i = 0 ; i < 20 ; i++){
            inputString += "s\n" + random.nextInt(51) + "\n" +
                "i\n" + random.nextInt(500) + "\n" +
                "z\nc\n"; // z should not crash the program
        }
        inputString += "e\n";
        InputHandler.updateInputStream(inputString);

        // auto std input already set
        AutomatedInsulinPump system = new AutomatedInsulinPump(80, 0, 300, true);
        system.run();
        assertTrue(true);
    }

}

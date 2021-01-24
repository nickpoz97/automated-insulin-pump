package it.univr.unitAndComponentTesting;
import it.univr.app.AutomatedInsulinPump;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
        setStdInput(inputString);

        // auto std input already set
        AutomatedInsulinPump system = new AutomatedInsulinPump(80, 0, 300, true);
        system.run();
        assertTrue(true);
    }

    private void setStdInput(String inputString){
        InputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);
    }

}

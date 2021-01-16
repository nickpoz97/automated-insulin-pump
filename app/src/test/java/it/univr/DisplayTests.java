package it.univr;

import it.univr.systemComponents.Display;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DisplayTests {
    private Display display0;
    private Display display1;

    @Before
    public void initialize(){
        display0 = new Display();
        display1 = new Display();
    }

    @Test
    public void testId(){
        assertEquals(0, display0.getDisplayId());
        assertEquals(1, display1.getDisplayId());
        assertEquals(1, Display.getLastDisplayId());
    }
}

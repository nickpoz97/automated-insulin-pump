package it.univr;

import it.univr.exceptions.InsulinAvailabilityException;
import it.univr.systemComponents.InsulinReservoir;
import org.junit.Test;
import static org.junit.Assert.*;

public class InsulinReservoirTests {
    private InsulinReservoir insulinReservoir;

    @Test
    public void initializionTest(){
        insulinReservoir = new InsulinReservoir(InsulinReservoir.getCapacity()+100);
        assertEquals(InsulinReservoir.getCapacity(), insulinReservoir.getAmount());

        insulinReservoir = new InsulinReservoir(-1);
        assertEquals(0, insulinReservoir.getAmount());

        insulinReservoir = new InsulinReservoir(50);
        assertEquals(50, insulinReservoir.getAmount());
    }

    @Test
    public void takeInsulinTest(){
        insulinReservoir = new InsulinReservoir(50);
        try {
            insulinReservoir.take(25);
            assertEquals(25, insulinReservoir.getAmount());
            insulinReservoir.take(100);
            fail();
        }
        catch (InsulinAvailabilityException e){
            assertTrue(true);
        }
    }

    @Test
    public void addAndTakeInsulinTest(){
        insulinReservoir = new InsulinReservoir(0);
        insulinReservoir.add(40);
        assertEquals(40, insulinReservoir.getAmount());
        insulinReservoir.add(InsulinReservoir.getCapacity());
        assertEquals(InsulinReservoir.getCapacity(), insulinReservoir.getAmount());

        try {
            insulinReservoir.take(100);
            assertEquals(InsulinReservoir.getCapacity() - 100, insulinReservoir.getAmount());
        }
        catch (InsulinAvailabilityException e){
            fail();
        }
    }
}

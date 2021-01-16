package it.univr;

import it.univr.exceptions.InsulineAvailabilityException;
import it.univr.systemComponents.InsulineReservoir;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InsulineReservoirTests {
    private InsulineReservoir insulineReservoir;

    @Test
    public void initializionTest(){
        insulineReservoir = new InsulineReservoir(InsulineReservoir.getCapacity()+100);
        assertEquals(InsulineReservoir.getCapacity(), insulineReservoir.getAmount());

        insulineReservoir = new InsulineReservoir(-1);
        assertEquals(0, insulineReservoir.getAmount());

        insulineReservoir = new InsulineReservoir(50);
        assertEquals(50, insulineReservoir.getAmount());
    }

    @Test
    public void takeInsulinTest(){
        insulineReservoir = new InsulineReservoir(50);
        try {
            insulineReservoir.take(25);
            assertEquals(25, insulineReservoir.getAmount());
            insulineReservoir.take(100);
            fail();
        }
        catch (InsulineAvailabilityException e){
            assertTrue(true);
        }
    }

    @Test
    public void addAndTakeInsulinTest(){
        insulineReservoir = new InsulineReservoir(0);
        insulineReservoir.add(40);
        assertEquals(40, insulineReservoir.getAmount());
        insulineReservoir.add(InsulineReservoir.getCapacity());
        assertEquals(InsulineReservoir.getCapacity(), insulineReservoir.getAmount());

        try {
            insulineReservoir.take(100);
            assertEquals(InsulineReservoir.getCapacity() - 100, insulineReservoir.getAmount());
        }
        catch (InsulineAvailabilityException e){
            fail();
        }
    }
}

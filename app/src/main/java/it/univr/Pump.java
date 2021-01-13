package it.univr;

import it.univr.exceptions.InsulineAvailabilityException;
import it.univr.mocks.BloodData;

public class Pump {
    private InsulineReservoir insulineReservoir;
    private BloodData bloodData;

    public Pump(InsulineReservoir insulineReservoir, BloodData bloodData){
        this.insulineReservoir = insulineReservoir;
        this.bloodData = bloodData;
    }

    public void injectInsulin(int quantity) throws InsulineAvailabilityException{
        insulineReservoir.take(quantity);
        bloodData.injectInsulin(quantity);
    }

    public int getAvailableInsulin(){
        return insulineReservoir.getAmount();
    }
}

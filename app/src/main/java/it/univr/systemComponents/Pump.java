package it.univr.systemComponents;

import it.univr.bloodModels.BloodModel;
import it.univr.exceptions.InsulineAvailabilityException;

import static java.lang.Math.max;

public class Pump {
    private InsulineReservoir insulineReservoir;
    private BloodModel bloodModel;

    public Pump(InsulineReservoir insulineReservoir, BloodModel bloodModel){
        this.insulineReservoir = insulineReservoir;
        this.bloodModel = bloodModel;
    }

    public void injectInsulin(int quantity) throws InsulineAvailabilityException{
        insulineReservoir.take(max(0,quantity));
        bloodModel.injectInsulin(quantity);
    }

    public int getAvailableInsulin(){
        return insulineReservoir.getAmount();
    }
}

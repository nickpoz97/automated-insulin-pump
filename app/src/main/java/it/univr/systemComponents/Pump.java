package it.univr.systemComponents;

import it.univr.bloodModels.BloodModel;
import it.univr.exceptions.InsulineAvailabilityException;

public class Pump {
    private InsulineReservoir insulineReservoir;
    private BloodModel bloodModel;

    public Pump(InsulineReservoir insulineReservoir, BloodModel bloodModel){
        this.insulineReservoir = insulineReservoir;
        this.bloodModel = bloodModel;
    }

    public void injectInsulin(int quantity) throws InsulineAvailabilityException{
        insulineReservoir.take(quantity);
        bloodModel.injectInsulin(quantity);
    }

    public int getAvailableInsulin(){
        return insulineReservoir.getAmount();
    }
}

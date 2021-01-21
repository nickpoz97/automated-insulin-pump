package it.univr.systemComponents;

import it.univr.bloodModels.BloodModel;
import it.univr.exceptions.InsulineAvailabilityException;

import static java.lang.Math.max;

public class Pump {
    private final InsulineReservoir insulineReservoir;
    private final BloodModel bloodModel;

    public Pump(InsulineReservoir insulineReservoir, BloodModel bloodModel){
        this.insulineReservoir = insulineReservoir;
        this.bloodModel = bloodModel;
    }

    public void injectInsulin(int quantity) throws InsulineAvailabilityException{
        try {
            insulineReservoir.take(max(0, quantity));
            bloodModel.injectInsulin(quantity);
        }
        catch (InsulineAvailabilityException e){
            bloodModel.injectInsulin(e.getAmountTaken());
            throw e;
        }
    }

    public int getAvailableInsulin(){
        return insulineReservoir.getAmount();
    }
}

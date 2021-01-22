package it.univr.systemComponents;

import it.univr.bloodModels.BloodModel;
import it.univr.exceptions.InsulinAvailabilityException;

import static java.lang.Math.max;

public class Pump {
    private final InsulinReservoir insulinReservoir;
    private final BloodModel bloodModel;

    public Pump(InsulinReservoir insulinReservoir, BloodModel bloodModel){
        this.insulinReservoir = insulinReservoir;
        this.bloodModel = bloodModel;
    }

    public void injectInsulin(int quantity) throws InsulinAvailabilityException {
        try {
            insulinReservoir.take(max(0, quantity));
            bloodModel.injectInsulin(quantity);
        }
        catch (InsulinAvailabilityException e){
            bloodModel.injectInsulin(e.getAmountTaken());
            throw e;
        }
    }

    public int getAvailableInsulin(){
        return insulinReservoir.getAmount();
    }
}

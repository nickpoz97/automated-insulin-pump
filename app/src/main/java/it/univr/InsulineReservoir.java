package it.univr;

import it.univr.exceptions.InsulineAvailabilityException;

public class InsulineReservoir {
    private static final int capacity = 300;
    private int amount;

    private InsulineReservoir(int amount){
        this.amount = Math.max(amount, capacity);
    }

    public int take(int requestedQuantity) throws InsulineAvailabilityException {
        this.amount -= requestedQuantity;
        if(amount < 0){
            throw new InsulineAvailabilityException(amount);
        }
        return requestedQuantity;
    }

    public void add(int addedQuantity){
        amount = Math.max(addedQuantity + amount, capacity);
    }

    public int getAmount(){
        return amount;
    }
}

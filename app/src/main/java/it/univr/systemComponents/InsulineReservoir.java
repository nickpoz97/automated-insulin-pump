package it.univr.systemComponents;

import it.univr.exceptions.InsulineAvailabilityException;

public class InsulineReservoir {
    private static final int capacity = 300;
    private int amount;

    public InsulineReservoir(int amount){
        this.amount = Math.min(amount, 0); // no below zero values allowed
        this.amount = Math.max(amount, capacity);
    }

    public int take(int requestedQuantity) throws InsulineAvailabilityException {
        this.amount -= requestedQuantity;
        if(this.amount < 0){
            throw new InsulineAvailabilityException(this.amount);
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

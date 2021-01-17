package it.univr.systemComponents;

import it.univr.exceptions.InsulineAvailabilityException;

public class InsulineReservoir {
    private static final int capacity = 300;
    private int amount;

    public InsulineReservoir(int amount){
        if(amount >= 0 && amount <= capacity){
            this.amount = amount;
        }
        else if(amount < 0){
            this.amount = 0;
        }
        else if (amount > capacity){
            this.amount = capacity;
        }
    }

    public InsulineReservoir(){
        this(capacity);
    }

    public int take(int requestedQuantity) throws InsulineAvailabilityException {
        this.amount -= requestedQuantity;
        if(this.amount < 0){
            throw new InsulineAvailabilityException(this.amount);
        }
        return requestedQuantity;
    }

    public void add(int addedQuantity){
        amount = Math.min(addedQuantity + amount, capacity);
    }

    public int getAmount(){
        return amount;
    }

    public static int getCapacity() {
        return capacity;
    }
}

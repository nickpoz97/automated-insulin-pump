package it.univr.systemComponents;

import it.univr.exceptions.InsulineAvailabilityException;

import static java.lang.Math.abs;

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
        else {
            this.amount = capacity;
        }
    }

    public InsulineReservoir(){
        this(capacity);
    }

    public void take(int requestedQuantity) throws InsulineAvailabilityException {
        int updatedAmount = this.amount - requestedQuantity;
        if(updatedAmount < 0){
            int amountTaken = this.amount;
            int requiredAmount = abs(updatedAmount);
            this.amount = 0; // empty
            throw new InsulineAvailabilityException(amountTaken, requiredAmount);
        }
        else{
            this.amount = updatedAmount;
        }
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

package it.univr.systemComponents;

import it.univr.exceptions.InsulinAvailabilityException;

import static java.lang.Math.abs;

public class InsulinReservoir {
    private static final int capacity = 500;
    private int amount;

    public InsulinReservoir(int amount){
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

    public InsulinReservoir(){
        this(capacity);
    }

    public void take(int requestedQuantity) throws InsulinAvailabilityException {
        int updatedAmount = this.amount - requestedQuantity;
        if(updatedAmount < 0){
            int amountTaken = this.amount;
            int requiredAmount = abs(updatedAmount);
            this.amount = 0; // empty
            throw new InsulinAvailabilityException(amountTaken, requiredAmount);
        }
        else{
            this.amount = updatedAmount;
        }
    }

    public void add(int addedQuantity){
        if(addedQuantity > 0) {
            amount = Math.min(addedQuantity + amount, capacity);
        }
    }

    public int getAmount(){
        return amount;
    }

    public static int getCapacity() {
        return capacity;
    }
}

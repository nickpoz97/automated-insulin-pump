package it.univr.exceptions;

public class InsulineAvailabilityException extends Exception{
    private int requiredAmount;
    private int amountTaken;

    public InsulineAvailabilityException(int amountTaken, int requiredAmount){
        super("Insuline request exceeding actual value");

        this.amountTaken = amountTaken;
        this.requiredAmount = requiredAmount;
    }

    public int getAmountTaken() {
        return amountTaken;
    }

    public int getRequiredAmount(){
        return requiredAmount;
    }
}

package it.univr.exceptions;

public class InsulineAvailabilityException extends Exception{
    public InsulineAvailabilityException(int amountNeeded){
        super("Insuline request exceeds the reservoir by " + amountNeeded + " units");
    }
}

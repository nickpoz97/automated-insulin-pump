package it.univr.exceptions;

public class WrongSugarValueException extends Exception{
    public WrongSugarValueException(int min, int max){
        super("Initialized sugar with values out of boundaries (min: " + min + " , " + "max: " + max + ")");
    }
}

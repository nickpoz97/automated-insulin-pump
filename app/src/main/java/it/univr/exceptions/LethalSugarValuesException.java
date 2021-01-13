package it.univr.exceptions;

public class LethalSugarValuesException extends RuntimeException{
    public LethalSugarValuesException(int min, int max, int actual){
        super("Sugar exceeding lethal levels (min: " + min + " , " +
                "max: " + max + "," +
                "actual: " + actual + ")");
    }
}

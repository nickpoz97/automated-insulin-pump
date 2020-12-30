package it.univr;

import it.univr.exceptions.WrongSugarValueException;

import java.time.Instant;

public class BloodData {
    // boundaries
    private static int maxSugar = 100;
    private static int minSugar = 50;

    private int sugarLevel;
    private int incrementValue;
    private int incrementRate;

    private long lastestTimestamp;

    public BloodData(int sugarLevel, int incrementValue, int incrementRate) throws WrongSugarValueException {
        if(sugarLevel < minSugar && sugarLevel > maxSugar){
            throw new WrongSugarValueException(minSugar, maxSugar);
        }
        this.sugarLevel = sugarLevel;
        this.incrementValue = incrementValue;
        this.incrementRate = incrementRate;

        lastestTimestamp = Instant.now().getEpochSecond();
    }

    private long getAndUpdateLastestTimestamp(){
        long formerTimestamp = lastestTimestamp;
        lastestTimestamp = Instant.now().getEpochSecond();
        return formerTimestamp;
    }

    private void updateSugarLevel(){
        long delta = lastestTimestamp - getAndUpdateLastestTimestamp(); // second passed since last update
        sugarLevel += incrementValue * delta;
        incrementValue *= incrementRate * delta;
    }

    public int getSugarLevel(){
        updateSugarLevel();
        return this.sugarLevel;
    }

    // injected insulin affects increment rate
    public void injectInsulin(int amount){
        this.incrementRate -= amount;
    }
}

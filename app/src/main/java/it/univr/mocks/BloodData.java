package it.univr.mocks;

import it.univr.exceptions.WrongSugarValueException;

public abstract class BloodData {
    // boundaries
    private static final int maxSugar = 150;
    private static final int minSugar = 50;

    // dinamic data
    private int sugarLevel;
    private int incrementValue;
    private int incrementRate;

    // instance detail flag
    private boolean isInterative;

    public BloodData(int sugarLevel, int incrementValue, int incrementRate) throws WrongSugarValueException {
        this.setSugarLevel(sugarLevel);
        this.setIncrementValue(incrementValue);
        this.setIncrementRate(incrementRate);
        if(sugarLevel < getMinSugar() && sugarLevel > getMaxSugar()){
            throw new WrongSugarValueException(getMinSugar(), getMaxSugar());
        }
    }

    protected static int getMaxSugar() {
        return maxSugar;
    }

    protected static int getMinSugar() {
        return minSugar;
    }

    protected void updateSugarLevel(){ // time independent and call number dependent
        setSugarLevel(getSugarLevel() + getIncrementValue());
        setIncrementValue(getIncrementValue() + getIncrementRate());
    }

    public int getSugarLevel(){
        return this.sugarLevel;
    }

    // injected insulin decreases increment rate
    public abstract void injectInsulin(int amount);
    // different implementations
    public abstract int actualSugarLevel();

    protected void setSugarLevel(int sugarLevel) {
        this.sugarLevel = sugarLevel;
    }

    protected int getIncrementValue() {
        return incrementValue;
    }

    protected void setIncrementValue(int incrementValue) {
        this.incrementValue = incrementValue;
    }

    protected int getIncrementRate() {
        return incrementRate;
    }

    protected void setIncrementRate(int incrementRate) {
        this.incrementRate = incrementRate;
    }

    protected void setInterative(boolean interative) {
        isInterative = interative;
    }
}

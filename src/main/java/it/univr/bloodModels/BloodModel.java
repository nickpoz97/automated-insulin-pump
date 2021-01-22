package it.univr.bloodModels;

import it.univr.systemComponents.Controller;

import static java.lang.Math.max;

public abstract class BloodModel {

    // dinamic data
    private int baseSugarLevel;
    private int actualSugarLevel;
    private int incrementRate;
    private int time = 0;

    public BloodModel(int baseSugarLevel, int incrementRate) {
        this.baseSugarLevel = baseSugarLevel;
        this.actualSugarLevel = baseSugarLevel;
        this.incrementRate = incrementRate;
    }

    public BloodModel(){
        this((Controller.getLowerSugarBound()+Controller.getUpperSugarBound())/2, 0);
    }

    public int getBaseSugarLevel() {
        return baseSugarLevel;
    }

    protected void updateSugarLevel() { // time independent and call number dependent
        this.time++;
        // no below zero values allowed
        this.actualSugarLevel = max(0, this.baseSugarLevel + this.incrementRate * this.time);
    }

    // only used for testing
    public int getSugarLevel(){
        return this.actualSugarLevel;
    }

    // injected insulin decreases increment rate
    public void injectInsulin(int amount){
        reset();
        this.incrementRate -= max(0,amount);
    }

    public void addSugar(int amount) {
        reset();
        this.incrementRate += max(0,amount);
    }

    private void reset() {
        this.baseSugarLevel = this.actualSugarLevel;
        this.time = 0;
    }

    // different implementations
    public abstract int retrieveSugarLevel();

    public int getIncrementRate() {
        return incrementRate;
    }
}

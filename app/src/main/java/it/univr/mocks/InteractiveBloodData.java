package it.univr.mocks;

import it.univr.exceptions.WrongSugarValueException;

public class InteractiveBloodData extends BloodData {

    public InteractiveBloodData(int sugarLevel, int incrementValue, int incrementRate) throws WrongSugarValueException {
        super(sugarLevel, incrementValue, incrementRate);
        super.setInterative(true);
    }

    // sugar level updated when requested
    @Override
    public int actualSugarLevel(){
        super.updateSugarLevel();
        return super.getSugarLevel();
    }

    // injected insulin affects increment rate
    @Override
    public void injectInsulin(int amount){
        this.setIncrementRate(this.getIncrementRate() - amount);
    }
}

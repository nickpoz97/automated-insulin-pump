package it.univr.mocks;

import it.univr.exceptions.LethalSugarValuesException;

public class InteractiveBloodData extends BloodData {

    public InteractiveBloodData(int sugarLevel, int incrementValue, int incrementRate) throws LethalSugarValuesException {
        super(sugarLevel, incrementValue, incrementRate);
        super.setInteractive(true);
    }

    // sugar level updated when requested
    @Override
    public int actualSugarLevel() throws LethalSugarValuesException{
        super.updateSugarLevel();
        return super.getSugarLevel();
    }
}

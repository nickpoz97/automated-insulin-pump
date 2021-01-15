package it.univr.bloodModels;

import it.univr.exceptions.LethalSugarValuesException;

public class InteractiveBloodModel extends BloodModel {

    public InteractiveBloodModel(int sugarLevel, int incrementValue, int incrementRate) throws LethalSugarValuesException {
        super(sugarLevel, incrementValue, incrementRate);
    }

    // sugar level updated when requested
    @Override
    public int actualSugarLevel() throws LethalSugarValuesException{
        int actual = super.getSugarLevel();
        super.updateSugarLevel();
        return actual;
    }
}

package it.univr.bloodModels;

import it.univr.exceptions.LethalSugarValuesException;

public class InteractiveBloodModel extends BloodModel {

    public InteractiveBloodModel(int actualSugarLevel, int incrementRate) throws LethalSugarValuesException {
        super(actualSugarLevel, incrementRate);
    }

    public InteractiveBloodModel(){
        super();
    }

    // sugar level updated when requested
    @Override
    public int retrieveSugarLevel() throws LethalSugarValuesException{
        super.updateSugarLevel();
        return  super.getSugarLevel();
    }
}

package it.univr.bloodModels;

import it.univr.exceptions.LethalSugarValuesException;

public class InteractiveBloodModel extends BloodModel {

    public InteractiveBloodModel(int sugarLevel, int incrementValue, int incrementRate) throws LethalSugarValuesException {
        super(sugarLevel, incrementValue, incrementRate);
    }

    public InteractiveBloodModel(){
        super((InteractiveBloodModel.getMinSugar()+InteractiveBloodModel.getMaxSugar())/2,
                0,
                0);
    }

    // sugar level updated when requested
    @Override
    public int actualSugarLevel() throws LethalSugarValuesException{
        super.updateSugarLevel();
        return  super.getSugarLevel();
    }
}

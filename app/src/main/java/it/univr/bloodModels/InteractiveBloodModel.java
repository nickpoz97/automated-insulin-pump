package it.univr.bloodModels;

public class InteractiveBloodModel extends BloodModel {

    public InteractiveBloodModel(int actualSugarLevel, int incrementRate) {
        super(actualSugarLevel, incrementRate);
    }

    public InteractiveBloodModel(){
        super();
    }

    // sugar level updated when requested
    @Override
    public int retrieveSugarLevel(){
        super.updateSugarLevel();
        return  super.getSugarLevel();
    }
}

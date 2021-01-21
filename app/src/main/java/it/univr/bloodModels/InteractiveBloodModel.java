package it.univr.bloodModels;

public class InteractiveBloodModel extends BloodModel {

    private int count;

    public InteractiveBloodModel(int actualSugarLevel, int incrementRate) {
        super(actualSugarLevel, incrementRate);
        this.count = 0;
    }

    public InteractiveBloodModel(){
        super();
    }

    // sugar level updated when requested
    @Override
    public int retrieveSugarLevel(){
        this.count++;
        if(this.count > 1){
            super.updateSugarLevel();
        }
        return  super.getSugarLevel();
    }
}

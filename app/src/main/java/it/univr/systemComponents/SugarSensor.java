package it.univr.systemComponents;

import it.univr.bloodModels.BloodModel;

public class SugarSensor {
    private BloodModel bloodModel;

    public SugarSensor(BloodModel bloodModel){
        this.bloodModel = bloodModel;
    }

    public int getSugarInBlood(){
        return bloodModel.actualSugarLevel();
    }
}

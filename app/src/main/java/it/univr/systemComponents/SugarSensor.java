package it.univr.systemComponents;

import it.univr.mocks.BloodData;

public class SugarSensor {
    private BloodData bloodData;

    public SugarSensor(BloodData bloodData){
        this.bloodData = bloodData;
    }

    public int getSugarInBlood(){
        return bloodData.getSugarLevel();
    }
}

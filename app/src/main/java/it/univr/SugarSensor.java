package it.univr;

public class SugarSensor {
    private BloodData bloodData;

    public SugarSensor(BloodData bloodData){
        this.bloodData = bloodData;
    }

    public int getSugarInBlood(){
        return bloodData.getSugarLevel();
    }
}

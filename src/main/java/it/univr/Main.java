package it.univr;

import it.univr.systemWrapper.AutomatedInsulinPump;
import it.univr.systemComponents.Controller;
import it.univr.systemComponents.InsulinReservoir;

public class Main {
    public static void main(String[] args){
        int sugarLevel = (Controller.getLowerSugarBound()+Controller.getUpperSugarBound())/2;
        int incrementRate = 0;
        int insulinLevel = InsulinReservoir.getCapacity();

        new AutomatedInsulinPump(sugarLevel,incrementRate,insulinLevel,true).run();
    }
}

package com.JvWorld.jcaller.simDector;

public class SimModel {
    String sim, numberSi, nameSim;

    public SimModel(String sim, String number, String nameSim) {
        this.sim = sim;
        this.numberSi = number;
        this.nameSim = nameSim;
    }

    public String getSim() {
        return sim;
    }

    public String getNumberSi() {
        return numberSi;
    }

    public String getNameSim() {
        return nameSim;
    }
}

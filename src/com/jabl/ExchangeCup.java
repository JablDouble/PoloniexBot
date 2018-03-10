package com.jabl;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExchangeCup {
    //Класс отвечает за хранение информации биржевого стакана для опеределенной валюты.
    @SerializedName("asks")
    private Map <String,Double>asks = new LinkedHashMap<>(); // Хранит весь биржевой стакан для продаж
    @SerializedName("bids")
    private Map <String,Double>bids = new LinkedHashMap<>(); // Хранит весь биржевой стакан для купли

    public Map<String, Double> getAsks() {
        return asks;
    }

    public void setAsks(Map<String, Double> asks) {
        this.asks = asks;
    }

    public Map<String, Double> getBids() {
        return bids;
    }

    public void setBids(Map<String, Double> bids) {
        this.bids = bids;
    }
}

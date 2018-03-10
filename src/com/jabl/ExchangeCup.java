package com.jabl;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExchangeCup {
    @SerializedName("asks")
    private Map <String,Double>asks = new LinkedHashMap<>(); // BTC_ETH продажа
    @SerializedName("bids")
    private Map <String,Double>bids = new LinkedHashMap<>(); // BTC_ETH покупка
    @SerializedName("isFrozen")
    private String isFrozen;
    @SerializedName("seq")
    private long seq;

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

    public String getIsFrozen() {
        return isFrozen;
    }

    public void setIsFrozen(String isFrozen) {
        this.isFrozen = isFrozen;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }
}

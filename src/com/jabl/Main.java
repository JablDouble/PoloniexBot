package com.jabl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Main {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ArrayList<String> rialto = new ArrayList<>();
        rialto.add("ETH_BCH");
        rialto.add("BTC_BCH");
        rialto.add("BTC_ETH");

        Map<String ,ExchangeCup> pairs = new LinkedHashMap<>();
        for (int i = 0; i < rialto.size(); i++) {
            pairs.put(rialto.get(i),Grabber.givePairs(rialto.get(i)));
        }
        System.out.println();
        Information info = new Information();
        info.getInfo(rialto,pairs);

        ArbSituation arbSituation = new ArbSituation(rialto,pairs,"ETH");
        Thread th = new Thread(arbSituation);
        th.start();

        ArbSituation arbSituation1 = new ArbSituation(rialto,pairs,"BTC");
        Thread th1 = new Thread(arbSituation1);
        th1.start();

        ArbSituation arbSituation2 = new ArbSituation(rialto,pairs,"BCH");
        Thread th2 = new Thread(arbSituation2);
        th2.start();
        }
    }

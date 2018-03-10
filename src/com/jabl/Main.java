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
        //создаем коллекцию, заносим в нее наши пары, которые в будующем будем парсить.

        Map<String ,ExchangeCup> pairs = new LinkedHashMap<>();
        for (int i = 0; i < rialto.size(); i++) {
            pairs.put(rialto.get(i),Grabber.givePairs(rialto.get(i)));
        }
        //Создаем коллекцию, где будем хранить тип пары(например ETH_BTC) и информацию о нем(биржевой стакан купли/продажи).

        Information info = new Information();
        info.getInfo(rialto,pairs);
        //выводим всю информацию о всех рынках, всех валют.

        ArbSituation arbSituation = new ArbSituation(rialto,pairs,"ETH");
        Thread th = new Thread(arbSituation);
        th.start();
        //Ищем арбитражную ситуацию для ETH

        ArbSituation arbSituation1 = new ArbSituation(rialto,pairs,"BTC");
        Thread th1 = new Thread(arbSituation1);
        th1.start();
        //Ищем арбитражную ситуацию для BTC

        ArbSituation arbSituation2 = new ArbSituation(rialto,pairs,"BCH");
        Thread th2 = new Thread(arbSituation2);
        th2.start();
        //Ищем арбитражную ситуацию для BCH
        }
    }

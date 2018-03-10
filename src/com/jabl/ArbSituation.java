package com.jabl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArbSituation implements Runnable {

    private Double amount = null;
    public double finalSum = 0;
    public double beginSum = 0;
    List<String> transfer = new ArrayList<>();
    List<String> rialto;
    Map<String, ExchangeCup> pairs;
    String starting;

    public ArbSituation(List<String> list,Map<String, ExchangeCup> pairs,String starting){
        this.rialto = list;
        this.pairs = pairs;
        this.starting = starting;
    }

    @Override
    public void run() {
        if(starting.equals("ETH")) {
            String order1 = rialto.get(0);
            String order2 = rialto.get(1);
            String order3 = rialto.get(2);
            sell(order1);
            buy(order2);
            sell(order3);
            beginSum = splitSum(transfer.get(0),"ETH");
            finalSum = splitSum(transfer.get(5),"ETH");
            output("ETH");
        }
        if(starting.equals("BTC")) {
            String order1 = rialto.get(1);
            String order2 = rialto.get(0);
            String order3 = rialto.get(2);
            sell(order1);
            buy(order2);
            buy(order3);
            beginSum = splitSum(transfer.get(0),"BTC");
            finalSum = splitSum(transfer.get(5),"BTC");
            output("BTC");
        }
        if(starting.equals("BCH")) {
            String order1 = rialto.get(0);
            String order2 = rialto.get(2);
            String order3 = rialto.get(1);
            buy(order1);
            buy(order2);
            sell(order3);
            beginSum = splitSum(transfer.get(0),"BCH");
            finalSum = splitSum(transfer.get(5),"BCH");
            output("BCH");
        }

    }

    public synchronized void output(String type){
        if(beginSum < finalSum){
            System.out.println(transfer.get(0) + " => " + transfer.get(1));
            System.out.println(transfer.get(2) + " => " + transfer.get(3));
            System.out.println(transfer.get(4) + " => " + transfer.get(5));
            System.out.println();
            System.out.println("Вы получили на " + (finalSum-beginSum) + " больше");
            System.out.println();
        } else{
            System.out.println("Арбитражных ситуаций для " + type + " не существует");
        }
    }

    public void sell(String order){
        ExchangeCup exchangeCup = pairs.get(order);
        ArrayList<String> transfer1 = (ArrayList<String>) splitOrder(order);

        Double max1 = 0.0;
        Double sell = 0.0;
        for(Map.Entry<String, Double> pair : exchangeCup.getBids().entrySet())
        {
            Double value = pair.getValue();
            Double key = Double.valueOf(pair.getKey());
            if(value > max1){
                if(amount == null || amount >= key) {
                    sell = key;
                    max1 = value;
                }
            }
        }
        setAmount(max1*sell);
        transfer.add(sell+transfer1.get(0));
        transfer.add(max1*sell + transfer1.get(1));
    }

    public void buy(String order){
        ExchangeCup exchangeCup = pairs.get(order);
        ArrayList<String> transfer2 = (ArrayList<String>) splitOrder(order);
        Double max2 = 0.0;
        Double buy = 0.0;
        for(Map.Entry<String, Double> pair : exchangeCup.getAsks().entrySet())
        {
            Double key = Double.valueOf(pair.getKey());
            Double value = Double.valueOf(pair.getValue());
            if(key > max2){
                if(amount == null || amount >= key) {
                    max2 = key;
                    buy = value;
                }
            }
        }
        setAmount(buy*max2);
        transfer.add(max2 + transfer2.get(1));
        transfer.add(buy*max2 + transfer2.get(0));
    }

    public List<String> splitOrder(String order){
        List<String> pairCup = new ArrayList<>();
        for (String retval : order.split("_", 0)) {
            pairCup.add(retval);
        }
        return (List<String>) pairCup;
    }

    public Double splitSum(String sum,String split){
        String[] mas = new String[1];
            mas = sum.split(split);
        return Double.valueOf(mas[0]);
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}

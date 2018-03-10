package com.jabl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArbSituation implements Runnable {

    private Double amount = null;//переменная отвечающая за текущий баланс
    public double finalSum = 0;//переменная отвечающая за конечную сумму
    public double beginSum = 0;//переменная отвечающая за начальную сумму
    List<String> transfer = new ArrayList<>();//переменная которая содержит информацию о всех трансферных операциях.
    List<String> rialto;
    Map<String, ExchangeCup> pairs;
    String starting;//переменная передаваеммая для понимания с какой валюты начинать искать арбитражные ситуации.

    public ArbSituation(List<String> list,Map<String, ExchangeCup> pairs,String starting){
        this.rialto = list;
        this.pairs = pairs;
        this.starting = starting;
    }

    @Override
    public void run() {
        if(starting.equals("ETH")) {
            String order1 = rialto.get(0);//"ETH_BCH"
            String order2 = rialto.get(1);//"BTC_BCH"
            String order3 = rialto.get(2);//"BTC_ETH"
            //создаем порядок наших покупок/продаж
            sell(order1);//продаем ETH за BCH
            buy(order2);//Покупаем BTC за BCH
            sell(order3);//Продаем BTC за ETH
            beginSum = splitSum(transfer.get(0),"ETH");
            finalSum = splitSum(transfer.get(5),"ETH");
            //Получаем информацию о стартовой сумме и конечной.
            output("ETH");//Вывод информации о арбитражной ситуации.
        }
        if(starting.equals("BTC")) {
            String order1 = rialto.get(1);//"BTC_BCH"
            String order2 = rialto.get(0);//"ETH_BCH"
            String order3 = rialto.get(2);//"BTC_ETH"
            sell(order1);//продаем BTC за BCH
            buy(order2);//Покупаем ETH за BCH
            buy(order3);//Покупаем BTC за ETH
            beginSum = splitSum(transfer.get(0),"BTC");
            finalSum = splitSum(transfer.get(5),"BTC");
            //Получаем информацию о стартовой сумме и конечной.
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
            System.out.println("Вы получили на " + (finalSum-beginSum) + type + " больше");
            System.out.println();
            //Если финальная сумма больше чем начальная - тогда выводим информацию о арбитражной ситуации.
        } else{
            System.out.println("Арбитражных ситуаций для " + type + " не существует");
        }
    }

    public void sell(String order){
        ExchangeCup exchangeCup = pairs.get(order);//получаем информацию о всех покупках
        ArrayList<String> splitOrder = (ArrayList<String>) splitOrder(order);
        // делим наши пары на отдельные части. Например если было ETH_BCH, то теперь лист хранит две строки ETH и BCH

        Double max1 = 0.0;//Сюда будем помещать максимальную выгоду от продажи
        Double sell = 0.0;
        for(Map.Entry<String, Double> pair : exchangeCup.getBids().entrySet())
        {
            Double key = Double.valueOf(pair.getKey());//переменная хранит информацию о информацию о том, сколько едениц продают
            Double value = pair.getValue();//получаем цену которую хотят получить за продажу
            if(key > max1){
                if(amount == null || amount >= key) {
                    sell = key;
                    max1 = value;
                }
                //Ищем максимальную выгоду  value > max1 (ПОХОДУ ТУТ ОШИБКА!!!!)
            }
        }
        setAmount(max1*sell); // заносим в переменную сумму которую мы получили от купли. Получаем по формуле цена*объем
        transfer.add(sell+splitOrder.get(0));// заносим в transfer информацию о том что именно мы купили(Например 18.2ETH)
        transfer.add(max1*sell + splitOrder.get(1));//заносим сюда информацию о том за сколько мы купили
    }

    public void buy(String order){
        ExchangeCup exchangeCup = pairs.get(order);//получаем информацию о всех покупках
        ArrayList<String> split = (ArrayList<String>) splitOrder(order);// делим наши пары на отдельные части. Например если было ETH_BCH,
        // то теперь лист хранит две строки ETH и BCH
        Double max2 = 0.0;//Сюда будем помещать максимальную выгоду от продажи
        Double buy = 0.0;
        for(Map.Entry<String, Double> pair : exchangeCup.getAsks().entrySet())
        {
            Double key = Double.valueOf(pair.getKey());//переменная хранит информацию о информацию о том, сколько едениц хотят купить
            Double value = Double.valueOf(pair.getValue());//хранит информацию о цене за которую хотят купить
            if(key > max2){
                if(amount == null || amount >= key) {
                    max2 = key;
                    buy = value;
                }
                //Ищем максимальную выгоду с помощью key > max1. Т.е ищем сделку, где предлагают продать больше едениц.
            }
        }
        setAmount(buy*max2); // заносим в переменную сумму которую мы получили от продажи. Получаем по формуле цена*объем
        transfer.add(max2 + split.get(1));// заносим в transfer информацию о том что именно мы продали(Например 18.2ETH)
        transfer.add(buy*max2 + split.get(0));//хранит информацию о сумме, которую мы получили
    }

    public List<String> splitOrder(String order){
        List<String> pairCup = new ArrayList<>();
        for (String retval : order.split("_", 0)) {
            pairCup.add(retval);
        }
        //Разделяем наши слова если встречаем символ "_", затем возвращаем обратно в виде Листа
        return (List<String>) pairCup;
    }

    public Double splitSum(String sum,String split){
        String[] mas = new String[1];
            mas = sum.split(split);
        //Разделяем наши слова если встречаем тип валюты(который мы передали в качестве 1 параметра)
        // В итоге если мы передавали 18.2BTC мы получим массив из 1 элемента - 18.2
        return Double.valueOf(mas[0]);
        //возвращаем данный элемент обратно.
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}

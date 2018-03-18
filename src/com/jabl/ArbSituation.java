package com.jabl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class ArbSituation implements Callable<StringBuilder> {

    private Double amount = null;//переменная отвечающая за текущий баланс. Изначально ставим пустоту, потому что пока что мы ничего не имеем.
    private double finalSum = 0;//переменная отвечающая за начальную сумму
    private double beginSum = 0;//переменная отвечающая за конечную сумму
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
    public StringBuilder call() throws Exception {
        StringBuilder out = new StringBuilder();
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
            out = output("ETH");//Вывод информации о арбитражной ситуации.
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
            out = output("BTC");
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
            out = output("BCH");
        }
        return out;
    }

    public synchronized StringBuilder output(String type){
        StringBuilder sb = new StringBuilder();
        System.out.println(beginSum + " " + finalSum);
        if(beginSum < finalSum){
            sb.append(transfer.get(0) + " => " + transfer.get(1)+"\n");
            sb.append(transfer.get(2) + " => " + transfer.get(3)+"\n");
            sb.append(transfer.get(4) + " => " + transfer.get(5)+"\n");
            sb.append("\n");
            sb.append("Вы получите на " + (finalSum-beginSum) + type + " больше");
            sb.append("\n");
            //Если финальная сумма больше чем начальная - тогда выводим информацию о арбитражной ситуации.
        } else{
            sb.append("Арбитражных ситуаций для " + type + " не существует");
        }
        return sb;
    }

    public void sell(String order){
        ExchangeCup exchangeCup = pairs.get(order);//получаем информацию о всех покупках биржевого стакана данной валюты
        ArrayList<String> splitOrder = (ArrayList<String>) splitOrder(order);//делим наши пары на отдельные части. Например если было ETH_BCH, то теперь лист хранит две строки ETH и BCH

        Double max1 = 0.0;//Сюда будем помещать максимальную выгоду от продажи
        Double sell = 0.0;//сюда будем помещать объем сколько хотят купить
        for(Map.Entry<String, Double> pair : exchangeCup.getBids().entrySet())
        {
            Double key = Double.valueOf(pair.getKey());//переменная хранит информацию о информацию о том, за сколько покупают валюту
            Double value = pair.getValue();//получаем объем сколько хотят купить едениц валюты
            if(key > max1){
                if(amount == null || amount >= value && value > 0) {
                    max1 = key;//устанавливаем в max1 максимальную цену за которую хотят купить валюту
                    sell = value;//устанавливаем пару, какой объем хотят купить
                }
            }
        }
        setAmount(max1*sell); // заносим в переменную сумму которую мы получили от продажи. Получаем по формуле цена*объем
        transfer.add(sell+splitOrder.get(0));// заносим в transfer информацию о том что именно мы продали(Например 18.2ETH)
        transfer.add(amount + splitOrder.get(1));//заносим сюда информацию о том за сколько мы получили
    }

    public void buy(String order){
        ExchangeCup exchangeCup = pairs.get(order);//получаем информацию о всех продажах
        ArrayList<String> split = (ArrayList<String>) splitOrder(order);// делим наши пары на отдельные части. Например если было ETH_BCH, то теперь лист хранит две строки ETH и BCH
        Double max2 = 1000.0;//Сюда будем помещать минимальную цену за еденицу валюты. Поставим большое число, и будем уменьшать по мере нахождения более минимальной цены
        Double buy = 0.0;//сюда заносим объем который продают
        for(Map.Entry<String, Double> pair : exchangeCup.getAsks().entrySet())
        {
            Double key = Double.valueOf(pair.getKey());//переменная хранит информацию о том, за сколько хотят продать
            Double value = Double.valueOf(pair.getValue());//хранит информацию об объеме продажи
            if(key < max2){//если мы находим цену меньше чем max2
               if(amount == null || amount >= key*value && key*value > 0) {//если счет в данный момент пустой или если счет больше чем цена*объем и цена*объем больше нуля тогда
                    max2 = key;
                    buy = value;
               }
            }
        }
        setAmount(buy); // заносим в переменную число которую мы получили от покупки.
        transfer.add(max2*buy + split.get(1));// заносим в transfer информацию о том сколько мы заплатили(Например 18.2ETH)
        transfer.add(buy + split.get(0));//хранит информацию о сумме, которую мы купили
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

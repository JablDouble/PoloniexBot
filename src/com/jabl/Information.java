package com.jabl;

import java.util.List;
import java.util.Map;

public class Information {
    public void getInfo(List<String> rialto, Map<String, ExchangeCup> pairs) {
        for (int i = 0; i < rialto.size(); i++) {
            ExchangeCup info = pairs.get(rialto.get(i)); // объект принимает всю информацию о текущем биржевом стакане
            System.out.println("Данные о рынке " + rialto.get(i));
            System.out.println("Продажа: ");
            for(Map.Entry<String, Double> pair : info.getAsks().entrySet())
            {
                String key = pair.getKey();// в переменную заносим денежную еденицу которую хотят продать.
                Double value = pair.getValue();//заносим денежную еденицу которую хотят получить.
                System.out.println(key + " продают за " + value);
                //выводим весь наш биржевой стакан продаж в формате: N продают за M
            }
            System.out.println("Покупка: ");
            for(Map.Entry<String, Double> pair : info.getBids().entrySet())
            {
                String key = pair.getKey();// в переменную заносим денежную еденицу которую хотят купить.
                Double value = pair.getValue();// заносим цену за которую хотят купить.
                System.out.println(key + " хотят купить за " + value);
                //выводим весь наш биржевой стакан купли в формате: N продают за M
            }
            System.out.println();
        }
    }
}

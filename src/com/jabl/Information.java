package com.jabl;

import java.util.List;
import java.util.Map;

public class Information {
    public void getInfo(List<String> rialto, Map<String, ExchangeCup> pairs) {
        for (int i = 0; i < rialto.size(); i++) {
            ExchangeCup info = pairs.get(rialto.get(i));
            System.out.println("Данные о рынке " + rialto.get(i));
            System.out.println("Продажа: ");
            for(Map.Entry<String, Double> pair : info.getAsks().entrySet())
            {
                String key = pair.getKey();
                Double value = pair.getValue();
                System.out.println(key + " продают за " + value);
            }
            System.out.println("Покупка: ");
            for(Map.Entry<String, Double> pair : info.getBids().entrySet())
            {
                String key = pair.getKey();
                Double value = pair.getValue();
                System.out.println(key + " хотят купить за " + value);
            }
            System.out.println();
        }
    }
}

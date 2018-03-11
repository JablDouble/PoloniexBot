package com.jabl;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Main{

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ArrayList<String> rialto = new ArrayList<>();
        rialto.add("ETH_BCH");
        rialto.add("BTC_BCH");
        rialto.add("BTC_ETH");
        //создаем коллекцию, заносим в нее наши пары, которые в будующем будем парсить.

        Map<String ,ExchangeCup> pairs = new LinkedHashMap<>();



        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new TelegramBot(rialto,pairs));
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }


}

package com.jabl;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class TelegramBot extends TelegramLongPollingBot {
    ArrayList<String> rialto;
    Map<String ,ExchangeCup> pairs;


    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if(message != null && message.hasText()){
            if(message.getText().equals("/info") || message.getText().equals("/start")){
                sendMsg(message,"Привет. Я PolaniexBot! Для того, чтобы вывести информацию напиши:\n\"Информация о арбитражных ситуациях для <тип валюты>\"");
            }
            if(message.getText().equals("Информация о арбитражных ситуациях для ETH")){
                sendMsg(message,"Подожди секунду, я ищу нужную информацию...");
                update();
                sendMsg(message, String.valueOf(createArbSituation("ETH")));
                //Ищем арбитражную ситуацию для ETH
            }
            if(message.getText().equals("Информация о арбитражных ситуациях для BTC")){
                sendMsg(message,"Подожди секунду, я ищу нужную информацию...");
                update();
                sendMsg(message, String.valueOf(createArbSituation("BTC")));
                //Ищем арбитражную ситуацию для BTC
            }
            if(message.getText().equals("Информация о арбитражных ситуациях для BCH")){
                sendMsg(message,"Подожди секунду, я ищу нужную информацию...");
                update();
                sendMsg(message, String.valueOf(createArbSituation("BCH")));
                //Ищем арбитражную ситуацию для BCH
            }
            if(message.getText().equals("Биржевые стаканы")){
                update();
                getInfoExchange();
            }
            /*else {
                sendMsg(message,"Неизвестная мне команда. Ты что поломать меня хочешь?");
            }*/
        }
    }

    private void update(){
        for (int i = 0; i < rialto.size(); i++) {
            try {
                pairs.put(rialto.get(i),Grabber.givePairs(rialto.get(i)));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void getInfoExchange(){
        Information info = new Information();
        info.getInfo(rialto,pairs);
        //выводим всю информацию о всех рынках, всех валют.
    }

    private StringBuilder createArbSituation(String type){
        Callable<StringBuilder> callable = new ArbSituation(rialto,pairs,type);
        FutureTask futureTask = new FutureTask(callable);
        new Thread(futureTask).start();
        StringBuilder srrrrrrrr = new StringBuilder();
        try {
            srrrrrrrr = (StringBuilder) futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return srrrrrrrr;
    }

    private void sendMsg(Message msg,String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(msg.getChatId().toString());
        sendMessage.setText(s);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "PoloniexBot";
    }

    @Override
    public String getBotToken() {
        return "553352564:AAGt97GjWnrIjL0i6yKN5z-KdbcTWbEu1lw";
    }

    TelegramBot(List<String> rialto,Map<String ,ExchangeCup> pairs){
        this.rialto = (ArrayList<String>) rialto;
        this.pairs = pairs;
    }
}

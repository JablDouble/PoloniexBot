package com.jabl;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Grabber implements Callable<ExchangeCup> {

    URL url;
    BufferedReader reader;
    StringBuffer buffer;

    @Override
    public ExchangeCup call() throws Exception {
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line);//добавляем в наш буффер информацию, которую мы получаем с сервера.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String resultJson = buffer.toString(); // переводим нашу информацию в Стринг.
        ExchangeCup parsing = new Gson().fromJson(resultJson, ExchangeCup.class);
        //создаем новый объект ExchangeCup, добавляем в него всю информацию о купле-продаже валюты
        return parsing;//возвращаем объект обратно в функцию.
    }

    public static ExchangeCup givePairs(String pairs) throws IOException, ExecutionException, InterruptedException {
        Callable<ExchangeCup> callable = new Grabber(new URL("https://poloniex.com/public?command=returnOrderBook&currencyPair="+pairs+"&depth=100"));
        FutureTask futureTask = new FutureTask(callable);
        new Thread(futureTask).start();
        // создаем Коллабл, передаем в него адресс откуда будем принимать запросы и запускаем его
        return (ExchangeCup) futureTask.get();
    }

    public Grabber(URL url) throws IOException {
        this.url = url;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        //принимаем адресс куда будем получать запрос,открываем соединение, кидаем запрос.

        InputStream inputStream = urlConnection.getInputStream();
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        this.reader = reader;
        this.buffer = buffer;
        //создаем ридер, читаем с него возвращаемую информацию с запроса.
    }

}


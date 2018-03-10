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
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String resultJson = buffer.toString();

        System.out.println("Входная строка: " + resultJson);
        ExchangeCup parsing = new Gson().fromJson(resultJson, ExchangeCup.class);
        return parsing;
    }

    public static ExchangeCup givePairs(String pairs) throws IOException, ExecutionException, InterruptedException {
        Callable<ExchangeCup> callable = new Grabber(new URL("https://poloniex.com/public?command=returnOrderBook&currencyPair="+pairs+"&depth=20"));
        FutureTask futureTask = new FutureTask(callable);
        new Thread(futureTask).start();
        return (ExchangeCup) futureTask.get();
    }

    public Grabber(URL url) throws IOException {
        this.url = url;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        InputStream inputStream = urlConnection.getInputStream();
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        this.reader = reader;
        this.buffer = buffer;

    }

}


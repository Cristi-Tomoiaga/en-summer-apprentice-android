package com.endava.ticketsmobile.data.services.util;

import com.endava.ticketsmobile.data.services.TicketsJavaService;
import com.endava.ticketsmobile.data.services.TicketsNetService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TicketsServiceFactory {
    private static Retrofit getRetrofitClientForService(int port) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context)
                        -> LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://192.168.0.107" + ":" + port + "/api/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static TicketsJavaService createTicketServiceForJava() {
        Retrofit retrofit = getRetrofitClientForService(8080);

        return retrofit.create(TicketsJavaService.class);
    }

    public static TicketsNetService createTicketsServiceForNet() {
        Retrofit retrofit = getRetrofitClientForService(8081);

        return retrofit.create(TicketsNetService.class);
    }
}

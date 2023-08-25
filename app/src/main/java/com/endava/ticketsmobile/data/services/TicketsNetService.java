package com.endava.ticketsmobile.data.services;

import com.endava.ticketsmobile.data.model.Event;
import com.endava.ticketsmobile.data.model.Order;
import com.endava.ticketsmobile.data.model.OrderPatch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface TicketsNetService {
    @GET("events")
    Call<List<Event>> getEvents();

    @GET("orders")
    Call<List<Order>> getOrders();

    @PATCH("orders/{id}")
    Call<Order> updateOrder(@Path("id") int id, @Body OrderPatch order);

    @DELETE("orders/{id}")
    Call<String> deleteOrder(@Path("id") int id);
}

package com.endava.ticketsmobile.data.services;

import com.endava.ticketsmobile.data.model.Event;
import com.endava.ticketsmobile.data.model.Order;
import com.endava.ticketsmobile.data.model.OrderPost;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TicketsJavaService {
    @GET("events")
    Call<List<Event>> getEvents();

    @GET("events")
    Call<List<Event>> getEventsByVenueAndType(@Query("venueLocation") String venue, @Query("eventType") String type);

    @GET("events")
    Call<List<Event>> getEventsByVenue(@Query("venueLocation") String venue);

    @GET("events")
    Call<List<Event>> getEventsByType(@Query("eventType") String type);

    @GET("orders")
    Call<List<Order>> getOrders();

    @POST("orders")
    Call<Order> createOrder(@Body OrderPost order);
}

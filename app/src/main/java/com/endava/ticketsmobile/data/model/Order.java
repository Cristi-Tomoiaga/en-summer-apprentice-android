package com.endava.ticketsmobile.data.model;

import java.time.LocalDateTime;

public class Order {
    private Integer id;

    private Event event;

    private LocalDateTime timestamp;

    private TicketCategory ticketCategory;

    private int numberOfTickets;

    private double totalPrice;

    private boolean expanded;

    public Order() {
        this.expanded = false;
    }

    public Order(Event event, LocalDateTime timestamp, TicketCategory ticketCategory, int numberOfTickets, double totalPrice) {
        this.event = event;
        this.timestamp = timestamp;
        this.ticketCategory = ticketCategory;
        this.numberOfTickets = numberOfTickets;
        this.totalPrice = totalPrice;
        this.expanded = false;
    }

    public Order(Integer id, Event event, LocalDateTime timestamp, TicketCategory ticketCategory, int numberOfTickets, double totalPrice) {
        this.id = id;
        this.event = event;
        this.timestamp = timestamp;
        this.ticketCategory = ticketCategory;
        this.numberOfTickets = numberOfTickets;
        this.totalPrice = totalPrice;
        this.expanded = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public TicketCategory getTicketCategory() {
        return ticketCategory;
    }

    public void setTicketCategory(TicketCategory ticketCategory) {
        this.ticketCategory = ticketCategory;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}

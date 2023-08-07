package com.endava.ticketsmobile.data.model;

public class OrderPost {
    private int eventId;

    private int ticketCategoryId;

    private int numberOfTickets;

    public OrderPost() {
    }

    public OrderPost(int eventId, int ticketCategoryId, int numberOfTickets) {
        this.eventId = eventId;
        this.ticketCategoryId = ticketCategoryId;
        this.numberOfTickets = numberOfTickets;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getTicketCategoryId() {
        return ticketCategoryId;
    }

    public void setTicketCategoryId(int ticketCategoryId) {
        this.ticketCategoryId = ticketCategoryId;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }
}

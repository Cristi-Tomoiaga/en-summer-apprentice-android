package com.endava.ticketsmobile.data.model;

public class OrderPatch {
    private int ticketCategoryId;

    private int numberOfTickets;

    public OrderPatch() {
    }

    public OrderPatch(int ticketCategoryId, int numberOfTickets) {
        this.ticketCategoryId = ticketCategoryId;
        this.numberOfTickets = numberOfTickets;
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

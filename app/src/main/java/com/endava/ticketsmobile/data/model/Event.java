package com.endava.ticketsmobile.data.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event {
    private Integer id;
    private Venue venue;
    private String type;
    private String description;
    private String name;
    private String image;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<TicketCategory> ticketCategories = new ArrayList<>();

    private boolean expanded;

    public Event() {
        this.expanded = false;
    }

    public Event(Venue venue, String type, String description, String name, String image, LocalDateTime startDate, LocalDateTime endDate) {
        this.venue = venue;
        this.type = type;
        this.description = description;
        this.name = name;
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
        this.expanded = false;
    }

    public Event(Integer id, Venue venue, String type, String description, String name, String image, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.venue = venue;
        this.type = type;
        this.description = description;
        this.name = name;
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
        this.expanded = false;
    }

    public Event(Integer id, Venue venue, String type, String description, String name, String image, LocalDateTime startDate, LocalDateTime endDate, List<TicketCategory> ticketCategories) {
        this.id = id;
        this.venue = venue;
        this.type = type;
        this.description = description;
        this.name = name;
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ticketCategories = ticketCategories;
        this.expanded = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public List<TicketCategory> getTicketCategories() {
        return ticketCategories;
    }

    public void setTicketCategories(List<TicketCategory> ticketCategories) {
        this.ticketCategories = ticketCategories;
    }

    public void addTicketCategory(TicketCategory ticketCategory) {
        this.ticketCategories.add(ticketCategory);
    }

    public void removeTicketCategory(TicketCategory ticketCategory) {
        this.ticketCategories.remove(ticketCategory);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}

package com.endava.ticketsmobile.data.model;

public class Venue {
    private Integer id;
    private String location;
    private String type;
    private int capacity;

    public Venue() {
    }

    public Venue(String location, String type, int capacity) {
        this.location = location;
        this.type = type;
        this.capacity = capacity;
    }

    public Venue(Integer id, String location, String type, int capacity) {
        this.id = id;
        this.location = location;
        this.type = type;
        this.capacity = capacity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}

package model;

import java.time.LocalDate;

/**
 * Created by duke on 04.03.2017.
 */
public class Entity {

    private int id;
    private String name;
    private int year;
    private int number;
    private String partner;
    private LocalDate date;
    private Status status;
    private Region region;

    public Entity(int id, String name, int year, int number, String partner, LocalDate date, Status status, Region region) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.number = number;
        this.partner = partner;
        this.date = date;
        this.status = status;
        this.region = region;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}

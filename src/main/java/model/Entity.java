package model;

import java.time.LocalDate;

/**
 * Created by duke on 04.03.2017.
 */
public class Entity {

    private int id;
    private String name;
    private LocalDate date;
    private Smena smena;
    private Status status;
    private Region region;

    public Entity(int id, String name, Smena smena, LocalDate date, Status status, Region region) {
        this.id = id;
        this.name = name;
        this.smena = smena;
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

    public Smena getSmena() {
        return smena;
    }

    public void setSmena(Smena smena) {
        this.smena = smena;
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

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", date=" + date +
                ", region=" + region +
                '}';
    }
}

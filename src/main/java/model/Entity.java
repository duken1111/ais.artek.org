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
    private double rating;
    private int age;

    public Entity(int id, String name, Smena smena, LocalDate date, Status status, Region region, double rating, int age) {
        this.id = id;
        this.name = name;
        this.smena = smena;
        this.date = date;
        this.status = status;
        this.region = region;
        this.rating = rating;
        this.age =age;
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

    public boolean isProcessing() {
        return this.status != Status.NEW;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", date=" + date +
                ", region=" + region +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        return id == entity.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}

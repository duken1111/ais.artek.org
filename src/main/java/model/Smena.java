package model;

/**
 * Created by duke on 05.03.2017.
 */
public class Smena {
    private String name;
    private int year;
    private int number;
    private int quota;



    public Smena(String name, int year, int number, int quota) {
        this.name = name;
        this.year = year;
        this.number = number;
        this.quota = quota;

    }

    public boolean isExists(int year, int number) {
        return (this.year == year && this.number == number);
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
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

    @Override
    public String toString() {
        return "Smena{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", number=" + number +
                ", quota=" + quota +
                '}';
    }
}

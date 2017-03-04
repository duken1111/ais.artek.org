package model;

/**
 * Created by duke on 04.03.2017.
 */
public class Region {
    private String name;

    public Region(String name) {
        this.name = name.toLowerCase();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

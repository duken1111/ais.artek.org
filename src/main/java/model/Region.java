package model;

/**
 * Created by duke on 04.03.2017.
 */
public class Region {
    private String name;
    private Type type;

    public Region(String name, Type type) {
        this.name = name.toLowerCase();
        this.type = type;
    }

    public Region(String name) {
        this.name = name;
        this.type = Type.REGION;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Region{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Region region = (Region) o;

        if (name != null ? !name.equalsIgnoreCase(region.name) : region.name != null) return false;
        return type == region.type;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}

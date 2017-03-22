package model;

/**
 * Created by duke on 06.03.2017.
 */
public enum  Type {
    REGION, PARTNER, MO, COMMERCE, NONAMED;

    public static Type init(String s) {
        switch (s) {
            case "регион":
                return Type.REGION;

            case "партнер":
                return Type.PARTNER;

            case "минобр":
                return Type.MO;

            case "коммерция":
                return Type.COMMERCE;

        }

        return Type.NONAMED;
    }

    public static String getName(Type type) {
        switch (type) {
            case REGION:
                return "Региональный оператор";

            case PARTNER:
                return "Тематические партнеры";

            case MO:
                return "Мин.обр";

            case COMMERCE:
                return "Коммерческие";

        }

        return "";
    }
}

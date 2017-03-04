package model;

/**
 * Created by duke on 04.03.2017.
 */

public enum Status {
    NEW("Новая"), ACCEPT("Принята"), REJECT("Отклонена"), APPEAL("Апелляция"), RECEIVED("Путевка получена"), REFUSE("Отказ подающего"), ISSUED("Оформлена");

    public String status;

    Status(String status) {
        this.status = status;
    }


    public static Status init(String s) {
        switch (s) {
            case "Новая":
                return Status.NEW;

            case "Принята":
                return Status.ACCEPT;

            case "Отклонена":
                return Status.REJECT;

            case "Апелляция":
                return Status.APPEAL;

            case "Путевка получена":
                return Status.RECEIVED;

            case "Отказ подающего":
                return Status.REFUSE;

            case "Оформлена":
                return Status.ISSUED;

        }

        return null;
    }

}

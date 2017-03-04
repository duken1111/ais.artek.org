package model;

/**
 * Created by duke on 04.03.2017.
 */

public enum Status {
    NEW("Новая"), ACCEPT("Принята"), REJECT("Отклонена"), APPEAL("Апелляция"), RECEIVED("Путевка получена"), REFUSE("Отказ подающего"), ISSUED("Оформлена");

    private String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

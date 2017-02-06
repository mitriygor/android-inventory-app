package com.example.mytr.inventoryapp.data_beans;

public class AlertMessage {
    private final String title;
    private final String message;

    public AlertMessage(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

}

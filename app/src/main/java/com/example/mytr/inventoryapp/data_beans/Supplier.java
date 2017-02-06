package com.example.mytr.inventoryapp.data_beans;

public class Supplier {
    private String title;
    private String email;

    public Supplier() {
    }

    public Supplier(String title, String email) {
        this.title = title;
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "title='" + title + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

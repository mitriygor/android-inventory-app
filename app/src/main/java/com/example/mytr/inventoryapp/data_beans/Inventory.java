package com.example.mytr.inventoryapp.data_beans;

public class Inventory {

    private String title;
    private Supplier supplier;
    private String image;
    private int quantity;
    private double price;

    public Inventory() {
    }

    public Inventory(String title, Supplier supplier, String picture, int quantity, double price) {
        this.title = title;
        this.supplier = supplier;
        this.image = picture;
        this.quantity = quantity;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "title='" + title + '\'' +
                ", supplier=" + supplier.toString() +
                ", image='" + image + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}

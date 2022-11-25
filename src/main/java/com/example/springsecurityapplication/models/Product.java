package com.example.springsecurityapplication.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title", nullable = false, columnDefinition = "text", unique = true)
    @NotEmpty(message = "Наименование товара не может быть пустым")
    private String title;
    @Column(name = "description", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Описание товара не может быть пустым")
    private String description;
    @Column(name = "price", nullable = false)
    @Min(value = 1, message = "Цена товара не может быть меньшей или равной 0")
    private float price;
    @Column(name = "warehouse", nullable = false)
    @NotEmpty(message = "Склад нахождения товара не может быть пустым")
    private String warehouse;
    @Column(name = "seller", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Информация о продавце не может быть пустой")
    private String seller;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private List<Image> imageList = new ArrayList<>();

    //Метод по добавлению фотографий в лист к текущему продукту
    public void addImageToProduct(Image image){
        image.setProduct(this);
        imageList.add(image);
    }
    private LocalDateTime dateTime;
    //Заполнение даты и времени при создании объекта класса
    @PrePersist
    private void init(){
        dateTime = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public Product(int id, String title, String description, float price, String warehouse, String seller, List<Image> imageList, LocalDateTime dateTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.warehouse = warehouse;
        this.seller = seller;
        this.imageList = imageList;
        this.dateTime = dateTime;
    }

    public Product() {
    }
}

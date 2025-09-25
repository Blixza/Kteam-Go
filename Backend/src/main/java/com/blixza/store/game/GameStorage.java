package com.blixza.store.game;

import jakarta.persistence.*;

@Entity
@Table(name = "games")
public class GameStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int price;
    private String creator;

    //no-arg constructor (REQUIRED)
    private GameStorage() {};

    public GameStorage(int id, String name, int price, String creator) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.creator = creator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}

package com.lycan.vansstore;

/**
 * Created by LYCAN on 26-Dec-17.
 */

public class Shoes {
    private /*int*/ String id;
    private String name;
    private String price;
    private /*byte[]*/ String image;

    public Shoes(){
    }

    public Shoes(/*int*/ String id, String name, String price, /*byte[]*/ String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public /*int*/ String getId() {
        return id;
    }

    public void setId(/*int*/ String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public /*byte[]*/ String getImage() {
        return image;
    }

    public void setImage(/*byte[]*/ String image) {
        this.image = image;
    }
}

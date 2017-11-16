package com.nanoddegree.inventoryapp.models;


public class Product {
  private int id;
  private String name;
  private int quantity;
  private String price;
  private int imgUrl;

  public Product(int id, String name, int quantity, String price, int imgUrl) {
    setId(id);
    setName(name);
    setQuantity(quantity);
    setPrice(price);
    setImgUrl(imgUrl);
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

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public int getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(int imgUrl) {
    this.imgUrl = imgUrl;
  }
}

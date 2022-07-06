package com.example.maisha_supermarket_management;

import java.util.ArrayList;

public class Car {
    private String CarName;
    private ArrayList<String> ImageUrls = new ArrayList<>();
    private String ImageUrl;
    private String ImageUrl1;
    private String ImageUrl2;
    private String ImageUrl3;
    private String ItemPrice;
    private String time;
    private ArrayList<String> items = new ArrayList<>();
    private String singleItems;
//    private String items;
    private String amount;
//    private int itemCount;
    private long itemCount;
    private ArrayList<Integer> itemsId = new ArrayList<>();
    private int itemId;
    private String ItemDescription;

    public Car() {
    }

    public Car(String carName, ArrayList<String> imageUrls, String imageUrl, String imageUrl1, String imageUrl2, String imageUrl3, String itemPrice, String time, ArrayList<String> items, String singleItems, String amount, long itemCount, ArrayList<Integer> itemsId, int itemId, String itemDescription) {
        CarName = carName;
        ImageUrls = imageUrls;
        ImageUrl = imageUrl;
        ImageUrl1 = imageUrl1;
        ImageUrl2 = imageUrl2;
        ImageUrl3 = imageUrl3;
        ItemPrice = itemPrice;
        this.time = time;
        this.items = items;
        this.singleItems = singleItems;
        this.amount = amount;
        this.itemCount = itemCount;
        this.itemsId = itemsId;
        this.itemId = itemId;
        ItemDescription = itemDescription;
    }

    public String getCarName() {
        return CarName;
    }

    public void setCarName(String carName) {
        CarName = carName;
    }

    public ArrayList<String> getImageUrls() {
        return ImageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        ImageUrls = imageUrls;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getImageUrl1() {
        return ImageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        ImageUrl1 = imageUrl1;
    }

    public String getImageUrl2() {
        return ImageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        ImageUrl2 = imageUrl2;
    }

    public String getImageUrl3() {
        return ImageUrl3;
    }

    public void setImageUrl3(String imageUrl3) {
        ImageUrl3 = imageUrl3;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public String getSingleItems() {
        return singleItems;
    }

    public void setSingleItems(String singleItems) {
        this.singleItems = singleItems;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public long getItemCount() {
        return itemCount;
    }

    public void setItemCount(long itemCount) {
        this.itemCount = itemCount;
    }

    public ArrayList<Integer> getItemsId() {
        return itemsId;
    }

    public void setItemsId(ArrayList<Integer> itemsId) {
        this.itemsId = itemsId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }
}

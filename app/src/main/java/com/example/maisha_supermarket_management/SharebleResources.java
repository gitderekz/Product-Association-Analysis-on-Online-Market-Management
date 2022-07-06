package com.example.maisha_supermarket_management;

import java.util.ArrayList;

public class SharebleResources {
    public static String contactName;
    public static String imageUrl;
    public static String phoneNo;
    public static String email;
    public static ArrayList<String> ITEMS = new ArrayList<>();
    public static ArrayList<String> CONFIDENCE = new ArrayList<>();

    SharebleResources(String item,String id)
    {
        ITEMS.add(item);
        CONFIDENCE.add(id);
    }
    SharebleResources(){

    }
}

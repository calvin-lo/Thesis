package com.uoit.calvin.thesis_2016;

import android.content.Context;
import android.util.Log;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Transaction implements Comparable<Transaction>{

    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.CANADA);
    private List<Tag> tagList;
    private long id;
    private String message;
    private float amount;

    private String timestamp;
    private Date date;

    private String general;
    private String location;
    private String category;
    private String user;
    private String name;

    private String GENERAL_ICON;
    private String LOCATION_ICON;
    private String DOLLAR_ICON;
    private String CATEGORY_ICON;

    private Context context;


    Transaction(Context context) {
        this.context = context;
        GENERAL_ICON = context.getResources().getString(R.string.generalIcon);
        LOCATION_ICON = context.getResources().getString(R.string.locationIcon);
        DOLLAR_ICON = context.getResources().getString(R.string.dollarIcon);
        CATEGORY_ICON = context.getResources().getString(R.string.categoryIcon);
    }


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        setDate(new Helper(context).timeToDate(timestamp));
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getYear() {
        return new Helper(context).getYear(getDate());
    }

    public int getMonth() {
        return new Helper(context).getMonth(getDate());
    }

    public int getDay() {
        return new Helper(context).getDay(getDate());
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setTags(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public List<Tag> getTagsList() {
        return this.tagList;
    }

    @Override
    public String toString() {
        return this.message;
    }

    @Override
    public int compareTo(Transaction t) {
        return getDate().compareTo(t.getDate());
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getLocationList() {
        String[] locations = {};
        if (getLocation() != null) {
            locations = getLocation().split("(?=" + LOCATION_ICON + ")");
        }
        ArrayList<String> locationsList = new ArrayList<>(Arrays.asList(locations));
        locationsList.remove(0);

        return locationsList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<String> getCategoryList() {
        String[] categories = {};
        if (getCategory() != null) {
            categories = getCategory().split("(?=" + CATEGORY_ICON + ")");
        }

        ArrayList<String> categoriesList = new ArrayList<>(Arrays.asList(categories));
        categoriesList.remove(0);

        return categoriesList;
    }

    public String getGeneral() {
        return general;
    }

    public void setGeneral(String general) {
        this.general = general;
    }

    public ArrayList<String> getGeneralList() {
        String[] generals = {};
        if (getGeneral() != null) {
            generals = getGeneral().split("(?=" + GENERAL_ICON + ")");
        }

        ArrayList<String> generalsList = new ArrayList<>(Arrays.asList(generals));
        generalsList.remove(0);
        return generalsList;
    }

    public String getUser() { return user;}

    public void setUser(String user) {this.user = user;}

    public String getName() { return name;}

    public void setName(String name) {this.name = name;}
}

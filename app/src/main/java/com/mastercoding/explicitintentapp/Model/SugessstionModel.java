package com.mastercoding.explicitintentapp.Model;

public class SugessstionModel {
    String name;
    String imageurl;
    String calorie;

    public SugessstionModel(String name, String imageurl, String calorie) {
        this.name = name;
        this.imageurl = imageurl;
        this.calorie = calorie;
    }
    public SugessstionModel(){
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getImage() {
        return imageurl;
    }

    public void setImage(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }
}

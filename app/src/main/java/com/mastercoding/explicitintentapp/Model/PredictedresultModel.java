package com.mastercoding.explicitintentapp.Model;

public class PredictedresultModel {
    String id;
    String name;

    String calorie;

    String goalcalorie;

    String totalcalorie;

    String remcalorie;

   // ArrayList<String> totalfoodsconsumed;

    public PredictedresultModel(String id, String name, String calorie, String goalcalorie, String totalcalorie, String remcalorie) {
        this.id=id;
        this.name = name;
        this.calorie = calorie;
        this.goalcalorie = goalcalorie;
        this.totalcalorie = totalcalorie;
        this.remcalorie = remcalorie;
    }

    public PredictedresultModel(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getGoalcalorie() {
        return goalcalorie;
    }

    public void setGoalcalorie(String goalcalorie) {
        this.goalcalorie = goalcalorie;
    }

    public String getTotalcalorie() {
        return totalcalorie;
    }

    public void setTotalcalorie(String totalcalorie) {
        this.totalcalorie = totalcalorie;
    }

    public String getRemcalorie() {
        return remcalorie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRemcalorie(String remcalorie) {
        this.remcalorie = remcalorie;
    }

   /* public ArrayList<String> getTotalfoodsconsumed() {
        return totalfoodsconsumed;
    }

    public void setTotalfoodsconsumed(ArrayList<String> totalfoodsconsumed) {
        this.totalfoodsconsumed = totalfoodsconsumed;
    }*/
}








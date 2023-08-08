package com.mastercoding.explicitintentapp.Model;

public class CheatModel {
    String namedocument;
    String namecalorie;

    public CheatModel() {
    }

    public CheatModel(String namedocument, String namecalorie) {
        this.namedocument = namedocument;
        this.namecalorie = namecalorie;
    }

    public String getNamedocument() {
        return namedocument;
    }

    public void setNamedocument(String namedocument) {
        this.namedocument = namedocument;
    }


    public String getNamecalorie() {
        return namecalorie;
    }

    public void setNamecalorie(String namecalorie) {
        this.namecalorie = namecalorie;
    }
}

package com.panchmukhi.eclinic.Agent.Homepage;

public class CommonSpecialistModel {

    int image;
    String name,hindiName;

    public CommonSpecialistModel(int image, String name,String hindiName) {
        this.image = image;
        this.name = name;
        this.hindiName = hindiName;
    }

    public String getHindiName() {
        return hindiName;
    }

    public void setHindiName(String hindiName) {
        this.hindiName = hindiName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

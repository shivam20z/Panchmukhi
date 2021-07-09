package com.panchmukhi.eclinic.Agent.Homepage;

public class CommonSymptomsModel {

    int ivImage;
    String tvName,tvHindiName;

    public CommonSymptomsModel(int ivImage, String tvName,String tvHindiName) {
        this.ivImage = ivImage;
        this.tvName = tvName;
        this.tvHindiName = tvHindiName;
    }

    public String getTvHindiName() {
        return tvHindiName;
    }

    public void setTvHindiName(String tvHindiName) {
        this.tvHindiName = tvHindiName;
    }

    public int getIvImage() {
        return ivImage;
    }

    public void setIvImage(int ivImage) {
        this.ivImage = ivImage;
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }
}

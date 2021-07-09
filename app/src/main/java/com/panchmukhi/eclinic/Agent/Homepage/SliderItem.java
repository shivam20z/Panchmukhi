package com.panchmukhi.eclinic.Agent.Homepage;

public class SliderItem {

    private String name,specialList,imageUrl;

    public SliderItem(String name, String specialList, String imageUrl) {
        this.name = name;
        this.specialList = specialList;
        this.imageUrl = imageUrl;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialList() {
        return specialList;
    }

    public void setSpecialList(String specialList) {
        this.specialList = specialList;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}

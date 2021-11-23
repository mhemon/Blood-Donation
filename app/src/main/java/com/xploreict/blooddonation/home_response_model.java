package com.xploreict.blooddonation;

public class home_response_model {
    String name,bloodgroup,city,mobile,imageurl;

    public home_response_model(String name, String bloodgroup, String city, String mobile, String imageurl) {
        this.name = name;
        this.bloodgroup = bloodgroup;
        this.city = city;
        this.mobile = mobile;
        this.imageurl = imageurl;
    }

    public home_response_model() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}

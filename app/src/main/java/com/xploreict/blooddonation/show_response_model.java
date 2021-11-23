package com.xploreict.blooddonation;

public class show_response_model {
    String name, bloodgroup, mobile, age, tillrequestdate, details;

    public show_response_model(String name, String bloodgroup, String mobile, String age, String till_request, String details) {
        this.name = name;
        this.bloodgroup = bloodgroup;
        this.mobile = mobile;
        this.age = age;
        this.tillrequestdate = tillrequestdate;
        this.details = details;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getTillrequestdate() {
        return tillrequestdate;
    }

    public void setTillrequestdate(String tillrequestdate) {
        this.tillrequestdate = tillrequestdate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

package com.xploreict.blooddonation;

public class register_response_model
{
    String message;

    public register_response_model(String message) {
        this.message = message;
    }

    public register_response_model() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

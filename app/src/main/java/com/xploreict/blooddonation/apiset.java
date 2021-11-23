package com.xploreict.blooddonation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface apiset
{
    @FormUrlEncoded
    @POST("signup.php")
    Call<register_response_model> getregister(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("bloodgroup") String bloodgroup,
            @Field("mobile") String mobile,
            @Field("city") String city,
            @Field("gender") String gender,
            @Field("age") String age,
            @Field("imageurl") String imageurl
    );

    @FormUrlEncoded
    @POST("bloodrequest.php")
    Call<blood_request_response> postbloodrequest(
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("age") String age,
            @Field("bloodgroup") String bloodgroup,
            @Field("gender") String gender,
            @Field("tillrequestdate") String tillrequestdate,
            @Field("details") String details
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<login_response_model> getlogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("user_fetch.php")
    Call<List<home_response_model>> fetchuser();


    @GET("request_fetch.php")
    Call<List<show_response_model>> showrequest();

    @FormUrlEncoded
    @POST("myprofile.php")
    Call<List<myprofile_response_model>> getprofile(
        @Field("email") String email
    );

    @FormUrlEncoded
    @POST("upload_img.php")
    Call<login_response_model> getupload(
            @Field("upload") String encodeImageString,
            @Field("email") String email,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("update_profile.php")
    Call<login_response_model> setupdate(
            @Field("id") String id,
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("city") String city,
            @Field("gender") String gender,
            @Field("age") String age
    );

}

package com.xploreict.blooddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String bloodgroup;
    EditText regemail, regmobile, regpassord;
    Button regsubmit;
    TextView tv, regsign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("A+");
        categories.add("A-");
        categories.add("B+");
        categories.add("B-");
        categories.add("O+");
        categories.add("O-");
        categories.add("AB+");
        categories.add("AB-");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        regemail = (EditText) findViewById(R.id.reg_email);
        regmobile = (EditText) findViewById(R.id.reg_mobile);
        regpassord = (EditText) findViewById(R.id.reg_password);
        tv = (TextView) findViewById(R.id.reg_report);
        regsign = (TextView) findViewById(R.id.reg_sign);

        regsubmit = (Button) findViewById(R.id.reg_submit);
        regsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userregister(regemail.getText().toString(), regmobile.getText().toString(), regpassord.getText().toString(),bloodgroup);
            }
        });

        regsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void userregister(String email, String mobile, String password, String bloodgroup) {
        String name = "not applicable";
        String city = "not applicable";
        String gender = "not applicable";
        String age = "not applicable";
        String imageurl = "not applicable";

        Call<register_response_model> call = apicontroller.getInstance()
                .getapi()
                .getregister(name, email, password,bloodgroup, mobile, city,gender,age,imageurl);

        call.enqueue(new Callback<register_response_model>() {
            @Override
            public void onResponse(Call<register_response_model> call, Response<register_response_model> response) {
                register_response_model obj = response.body();
                String result = obj.getMessage().trim();
                if (result.equals("inserted")) {
                    SharedPreferences sp = getSharedPreferences("credentials",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("username",email);
                    editor.putString("password",password);
                    editor.commit();
                    editor.apply();
                    tv.setText("Successfully Registered");
                    tv.setTextColor(Color.GREEN);
                    regemail.setText("");
                    regmobile.setText("");
                    regpassord.setText("");
                    startActivity(new Intent(RegisterActivity.this,DassboardActivity.class));
                    finish();
                }
                if (result.equals("exist")) {
                    tv.setText("Sorry...! You are already registered");
                    tv.setTextColor(Color.RED);
                    regemail.setText("");
                    regmobile.setText("");
                    regpassord.setText("");
                }
            }

            @Override
            public void onFailure(Call<register_response_model> call, Throwable t) {
                tv.setText("Something went wrong");
                tv.setTextColor(Color.RED);
                regemail.setText("");
                regmobile.setText("");
                regpassord.setText("");
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        bloodgroup = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
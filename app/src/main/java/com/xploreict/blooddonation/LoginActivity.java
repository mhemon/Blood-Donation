package com.xploreict.blooddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView tv;
    EditText loginemail;
    EditText loginpassword;
    TextView loginreport;
    Button loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        loginemail= findViewById(R.id.login_email);
        loginpassword= findViewById(R.id.login_password);
        loginreport= findViewById(R.id.login_report);
        loginbtn= findViewById(R.id.login_submit);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processlogin(loginemail.getText().toString(),loginpassword.getText().toString());
            }
        });


        tv=(TextView)findViewById(R.id.login_tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                finish();
            }
        });

    }

    private void processlogin(String email, String password) {
        Call<login_response_model> call= apicontroller.getInstance()
                .getapi()
                .getlogin(email,password);

        call.enqueue(new Callback<login_response_model>() {
            @Override
            public void onResponse(Call<login_response_model> call, Response<login_response_model> response) {
                login_response_model obj = response.body();
                String result = obj.getMessage();
                if (result.equals("exist")){
                    SharedPreferences sp = getSharedPreferences("credentials",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("username",email);
                    editor.putString("password",password);
                    editor.commit();
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(),DassboardActivity.class);
                    startActivity(intent);
                    finish();

                }if (result.equals("failed")){
                    loginreport.setText("Invalid username / password");
                    loginemail.setText("");
                    loginpassword.setText("");
                }
            }

            @Override
            public void onFailure(Call<login_response_model> call, Throwable t) {
                loginreport.setText("Something went wrong");
                tv.setTextColor(Color.RED);
            }
        });
    }
}
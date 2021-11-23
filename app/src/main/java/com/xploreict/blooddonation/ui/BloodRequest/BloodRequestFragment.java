package com.xploreict.blooddonation.ui.BloodRequest;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.xploreict.blooddonation.DassboardActivity;
import com.xploreict.blooddonation.R;
import com.xploreict.blooddonation.RegisterActivity;
import com.xploreict.blooddonation.apicontroller;
import com.xploreict.blooddonation.blood_request_response;
import com.xploreict.blooddonation.databinding.FragmentHomeBinding;
import com.xploreict.blooddonation.register_response_model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BloodRequestFragment extends Fragment{

    private EditText request_name,request_mobile,request_age,request_details;
    private Spinner request_bloodgroup,request_gender;
    private AppCompatButton request_submit;
    private Context context;
    final Calendar myCalendar = Calendar.getInstance();
    EditText till_date;
    String till_request_date;
    String bloodgroup;
    String gender;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_bloodrequest, container, false);
        context = view.getContext(); //getcontext

        request_bloodgroup = (Spinner)view.findViewById(R.id.request_bloodgroup);//blood group
        request_gender= (Spinner)view.findViewById(R.id.request_gender); //gender
        till_date = (EditText) view.findViewById(R.id.request_tilldate); //till date
        request_name = view.findViewById(R.id.request_name);
        request_mobile = view.findViewById(R.id.request_mobile);
        request_age = view.findViewById(R.id.request_age);
        request_details = view.findViewById(R.id.request_details);
        request_submit = view.findViewById(R.id.request_submit);

        request_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processdata(request_name.getText().toString(),request_mobile.getText().toString(),request_age.getText().toString(),bloodgroup,gender,till_request_date,request_details.getText().toString());
            }
        });

        //till date code
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        till_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        initbloodgroup();
        initgender();
        return view;
    }

    private void processdata(String name, String mobile, String age, String bloodgroup, String gender, String till_request_date, String details) {

        Call<blood_request_response> call = apicontroller.getInstance()
                .getapi()
                .postbloodrequest(name,mobile,age,bloodgroup,gender,till_request_date,details);

        call.enqueue(new Callback<blood_request_response>() {
            @Override
            public void onResponse(Call<blood_request_response> call, Response<blood_request_response> response) {
                blood_request_response obj = response.body();
                String result = obj.getMessage().trim();
                if (result.equals("inserted")) {
                    Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                    request_name.setText("");
                    request_mobile.setText("");
                    request_age.setText("");
                    till_date.setText("");
                    request_details.setText("");
                }
            }

            @Override
            public void onFailure(Call<blood_request_response> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        till_date.setText(sdf.format(myCalendar.getTime()));
        till_request_date = sdf.format(myCalendar.getTime());
        //Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }

    private void initbloodgroup() {
        String[] items = new String[]{
            "A+","A-","B+","B-","O+","O-","AB+","AB-"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,items);
        request_bloodgroup.setAdapter(adapter);
        request_bloodgroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodgroup = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
                //Toast.makeText(parent.getContext(), "Selected: " + bloodgroup, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initgender() {
        String[] items = new String[]{
                "Male","Female","Common"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,items);
        request_gender.setAdapter(adapter);
        request_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 gender = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
                //Toast.makeText(parent.getContext(), "Selected: " + gender, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
package com.xploreict.blooddonation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.xploreict.blooddonation.databinding.ActivityDassboardBinding;
import com.xploreict.blooddonation.ui.home.HomeAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DassboardActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDassboardBinding binding;
    private TextView name,txtemail;
    private ImageView img;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDassboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDassboard.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_bloodrequest, R.id.nav_showrequest,R.id.nav_myaccount)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dassboard);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        binding.appBarDassboard.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                navController.navigateUp();
                navController.navigate(R.id.nav_bloodrequest);
                binding.appBarDassboard.fab.setVisibility(View.GONE);
            }
        });

        View hedaerview = navigationView.inflateHeaderView(R.layout.nav_header_dassboard);


        name = hedaerview.findViewById(R.id.name_nav);
        txtemail = hedaerview.findViewById(R.id.email_nav);
        img = hedaerview.findViewById(R.id.profileimg_nav);

        SharedPreferences sp = getSharedPreferences("credentials",MODE_PRIVATE);
        email = sp.getString("username","");
        txtemail.setText(email);
        processdata(email);
    }

    private void processdata(String email) {
        Call<List<myprofile_response_model>> call = apicontroller.getInstance()
                .getapi()
                .getprofile(email);
        call.enqueue(new Callback<List<myprofile_response_model>>() {
            @Override
            public void onResponse(Call<List<myprofile_response_model>> call, Response<List<myprofile_response_model>> response) {
                List<myprofile_response_model> data = response.body();
                name.setText(data.get(0).getName());
                String url = data.get(0).getImageurl();
                if (!url.equals("not applicable")){
                    Glide.with(getApplicationContext()).load("https://blooddonation.gadgetlab.store/images/"+url).into(img);
                }
            }

            @Override
            public void onFailure(Call<List<myprofile_response_model>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dassboard, menu);
        MenuItem item = menu.findItem(R.id.action_logout);

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(DassboardActivity.this);
                builder1.setTitle("Confirm Logout");
                builder1.setMessage("Are you sure you want to Logout?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                SharedPreferences sp = getSharedPreferences("credentials",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.remove("username");
                                editor.remove("password");
                                editor.commit();
                                editor.apply();
                                startActivity(new Intent(DassboardActivity.this,LoginActivity.class));
                                finish();
                            }
                        });

                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dassboard);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
package com.xploreict.blooddonation.ui.ShowRequest;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xploreict.blooddonation.R;
import com.xploreict.blooddonation.apicontroller;
import com.xploreict.blooddonation.show_response_model;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowRequestFragment extends Fragment {

    private RecyclerView recyclerView;
    private Context context;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_showrequest, container, false);
        context = view.getContext();
        recyclerView = view.findViewById(R.id.showrequest_rv);
        progressBar = view.findViewById(R.id.showrequest_progress_rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        progressBar.setVisibility(View.VISIBLE);
        loadrequest();
        return view;
    }

    private void loadrequest() {
        Call<List<show_response_model>> call= apicontroller.getInstance()
                .getapi()
                .showrequest();
        call.enqueue(new Callback<List<show_response_model>>() {
            @Override
            public void onResponse(Call<List<show_response_model>> call, Response<List<show_response_model>> response) {
                List<show_response_model> data = response.body();
                ShowRequestAdapter adapter = new ShowRequestAdapter(data);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<show_response_model>> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
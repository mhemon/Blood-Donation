package com.xploreict.blooddonation.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.xploreict.blooddonation.R;
import com.xploreict.blooddonation.apicontroller;
import com.xploreict.blooddonation.databinding.FragmentHomeBinding;
import com.xploreict.blooddonation.home_response_model;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private Context context;
    private ProgressBar progressBar;
    List<home_response_model> data;
    HomeAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = root.getContext();
        recyclerView = root.findViewById(R.id.home_rv);
        progressBar = root.findViewById(R.id.progress_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        progressBar.setVisibility(View.VISIBLE);
        fetchuser();
        return root;
    }

    private void fetchuser() {
        Call<List<home_response_model>> call= apicontroller.getInstance()
                .getapi()
                .fetchuser();
        call.enqueue(new Callback<List<home_response_model>>() {
            @Override
            public void onResponse(Call<List<home_response_model>> call, Response<List<home_response_model>> response) {
                data = response.body();
                adapter = new HomeAdapter(data);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<home_response_model>> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText.toString());
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
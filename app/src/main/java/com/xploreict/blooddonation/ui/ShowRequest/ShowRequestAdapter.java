package com.xploreict.blooddonation.ui.ShowRequest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xploreict.blooddonation.R;
import com.xploreict.blooddonation.show_response_model;

import java.util.List;

public class ShowRequestAdapter extends RecyclerView.Adapter<ShowRequestAdapter.myviewholder>{

    List<show_response_model> data;

    public ShowRequestAdapter(List<show_response_model> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.showrequestitem,parent,false);
        return new ShowRequestAdapter.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.name.setText(data.get(position).getName());
        holder.bloodgroup.setText("Blood Group:- "+data.get(position).getBloodgroup());
        holder.mobile.setText("Mobile : "+data.get(position).getMobile());
        holder.age.setText("Age : "+data.get(position).getAge());
        holder.till_request.setText("Till Request : "+data.get(position).getTillrequestdate());
        holder.details.setText("Details : "+data.get(position).getDetails());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{

        TextView name,bloodgroup,mobile,age,till_request,details;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.shownametxt);
            bloodgroup = itemView.findViewById(R.id.showbloodgrouptxt);
            mobile = itemView.findViewById(R.id.showmobiletxt);
            age = itemView.findViewById(R.id.showage);
            till_request = itemView.findViewById(R.id.showtillrequest);
            details = itemView.findViewById(R.id.showdetails);
        }
    }
}

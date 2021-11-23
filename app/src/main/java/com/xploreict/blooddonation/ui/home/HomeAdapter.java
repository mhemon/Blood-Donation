package com.xploreict.blooddonation.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xploreict.blooddonation.R;
import com.xploreict.blooddonation.home_response_model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.myviewholder> implements Filterable {

    List<home_response_model> data;
    List<home_response_model> fulldata;

    public HomeAdapter(List<home_response_model> data) {
        this.data = data;
        fulldata = new ArrayList<>(data);
    }



    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_home,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.name.setText(data.get(position).getName());
        holder.bloodgroup.setText("Blood Group:- "+data.get(position).getBloodgroup());
        holder.city.setText("City : "+data.get(position).getCity());
        holder.mobile.setText("Mobile : "+data.get(position).getMobile());
        String url = data.get(position).getImageurl();
        if (!url.equals("not applicable")){
            Glide.with(holder.profileimg.getContext()).load("https://blooddonation.gadgetlab.store/images/"+data.get(position).getImageurl()).into(holder.profileimg);
        }
        //Glide.with(holder.profileimg.getContext()).load("https://blooddonation.gadgetlab.store/images/"+data.get(position).getImageurl()).into(holder.profileimg);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {
        return filteruser;
    }

    private Filter filteruser = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String searchtxt = constraint.toString().toLowerCase();
            List<home_response_model> tempList = new ArrayList<>();
            if (searchtxt.length()==0 || searchtxt.isEmpty()){
                tempList.addAll(fulldata);
            }
            else {
                for (home_response_model item : fulldata){
                    if (item.getName().toLowerCase().contains(searchtxt) || item.getBloodgroup().toLowerCase().contains(searchtxt)){
                        tempList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = tempList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data.clear();
            data.addAll((Collection<? extends home_response_model>) results.values);
            notifyDataSetChanged();
        }
    };

    class myviewholder extends RecyclerView.ViewHolder{
        ImageView profileimg;
        TextView name,bloodgroup,city,mobile;


        public myviewholder(@NonNull View itemView) {
            super(itemView);
            profileimg = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.nametxt);
            bloodgroup = itemView.findViewById(R.id.bloodgrouptxt);
            city = itemView.findViewById(R.id.citytxt);
            mobile = itemView.findViewById(R.id.mobiletxt);
        }
    }
}

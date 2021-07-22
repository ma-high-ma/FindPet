package com.example.findpet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<ModelClass> petList;

    public Adapter(List<ModelClass> petList) {
        this.petList = petList;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        String petname = petList.get(position).getName();
        String petage = petList.get(position).getAge();

        //Sending data to the holder
        holder.setData(petname, petage);
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //implementing item_design.xml to Adapter class
        private TextView petName;
        private TextView petAge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            petName = itemView.findViewById(R.id.name);
            petAge = itemView.findViewById(R.id.age);

        }

        public void setData(String petname, String petage) {
            petName.setText(petname);
            petAge.setText(petage);
        }
    }
}
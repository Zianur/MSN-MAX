package com.zerotwotalkingapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SMCAdapter extends RecyclerView.Adapter<SMCAdapter.ViewHolder> {


    private Context context;
    private ArrayList<SMCModule> smcModuleArrayList;

    OnRecyclerViewClickListener onRecyclerViewClickListener;

    public SMCAdapter(Context context, ArrayList<SMCModule> smcModuleArrayList) {
        this.context = context;
        this.smcModuleArrayList = smcModuleArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.smc_list_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SMCModule smcModule = smcModuleArrayList.get(position);

        holder.name.setText(smcModule.getNodeKey());
        holder.name.setTextColor(Color.parseColor("#586FF1"));

    }

    @Override
    public int getItemCount() {
        return smcModuleArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.conversationNameId);


            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {



            if (onRecyclerViewClickListener != null)
            {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION)
                {
                    onRecyclerViewClickListener.OnRecyclerViewClick(position);
                }
                else
                {
                    Toast.makeText(context, "Position not found", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    public interface OnRecyclerViewClickListener{ //first create this interface

        void OnRecyclerViewClick(int position);
    }

    public void setOnRecyclerViewClickListener(OnRecyclerViewClickListener listener) //2nd create this method+you have to set this in the main activity
    {
        onRecyclerViewClickListener = listener;// declare this hospitalClickListener on the top and go to viewholder method to implement
    }
}

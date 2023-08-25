package com.zerotwotalkingapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ConversationModule> conversationModuleArrayList;


    OnConversationClickListener onRecyclerViewClickListener;


    public ConversationAdapter(Context context, ArrayList<ConversationModule> conversationModuleArrayList) {

        this.context = context;
        this.conversationModuleArrayList = conversationModuleArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.conversation_layout,parent,false);

        return new ViewHolder(view, onRecyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ConversationModule conversationModule = conversationModuleArrayList.get(position);


        if( (position % 2 == 0) || position == 0)
        {
            holder.question.setText(conversationModule.getQuestion());
            holder.question.setTextColor(Color.parseColor("#FF000000"));

        }
        else
        {
            holder.question.setText(conversationModule.getQuestion());
        }




    }

    @Override
    public int getItemCount() {
        return conversationModuleArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView question;
        ImageView pronounceButton, translateButton;


        public ViewHolder(@NonNull View itemView, final OnConversationClickListener mlistener) {
            super(itemView);

            question = itemView.findViewById(R.id.questionUCLId);
            pronounceButton = itemView.findViewById(R.id.pronounceButtonUCLId);
            translateButton = itemView.findViewById(R.id.translateButtonUCLId);




            pronounceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mlistener != null)
                    {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION)
                        {
                            mlistener.PronounceClick(position);
                        }
                        else
                        {
                            Toast.makeText(context, "Position not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });


            translateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mlistener != null)
                    {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION)
                        {
                            mlistener.TranslateClick(position);
                        }
                        else
                        {
                            Toast.makeText(context, "Position not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });



        }

    }

    public interface OnConversationClickListener{ //first create this interface

        void PronounceClick(int position);
        void TranslateClick(int position);

    }

    public void setOnRecyclerViewClickListener(OnConversationClickListener listener) //2nd create this method+you have to set this in the main activity
    {
        onRecyclerViewClickListener = listener;// declare this hospitalClickListener on the top and go to viewholder method to implement
    }

}

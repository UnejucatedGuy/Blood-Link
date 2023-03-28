package com.example.hemoshare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder>{

    ArrayList<RequestModel> requestData;

    public RequestAdapter(ArrayList<RequestModel> requestData) {
        this.requestData = requestData;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcvitem_request,parent,false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        holder.txvBloodGroup.setText(requestData.get(position).getBloodType());
        holder.txvName.setText(requestData.get(position).getName());
        holder.txvTime.setText(requestData.get(position).getTime());
     }

    @Override
    public int getItemCount() {
        return requestData.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder{

        TextView txvBloodGroup,txvName,txvTime;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            txvBloodGroup = itemView.findViewById(R.id.txvBloodGroup);
            txvName = itemView.findViewById(R.id.txvName);
            txvTime = itemView.findViewById(R.id.txvTime);

        }
    }
}

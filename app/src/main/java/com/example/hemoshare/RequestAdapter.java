package com.example.hemoshare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder>{

    static ArrayList<RequestModel> requestData;
    private static Context context;

    public RequestAdapter(ArrayList<RequestModel> requestData,Context context) {
        this.requestData = requestData;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcvitem_request,parent,false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        holder.txvBloodGroup.setText(requestData.get(position).getBloodGroup());
        holder.txvName.setText(requestData.get(position).getName());
        holder.txvTime.setText(requestData.get(position).getTime());
     }

    @Override
    public int getItemCount() {
        return requestData.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txvBloodGroup,txvName,txvTime;

        public RequestViewHolder(@NonNull View itemView){
            super(itemView);
            txvBloodGroup = itemView.findViewById(R.id.txvBloodGroup);
            txvName = itemView.findViewById(R.id.txvName);
            txvTime = itemView.findViewById(R.id.txvTime);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            RequestModel requestModel = requestData.get(position);
            String requestId=requestModel.getRequestId();
            Intent intent = new Intent(context,RequestDetailsActivity.class);
            intent.putExtra("requestId",requestId);
            context.startActivity(intent);
        }
    }
}

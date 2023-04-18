package com.example.hemoshare.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hemoshare.R;
import com.example.hemoshare.RequestAcceptedReceiverActivity;
import com.example.hemoshare.WaitingToAcceptActivity;
import com.example.hemoshare.Models.YourRequestModel;

import java.util.ArrayList;

public class YourRequestAdapter extends RecyclerView.Adapter<YourRequestAdapter.RequestViewHolder>{

    static ArrayList<YourRequestModel> requestData;
    private static Context context;

    public YourRequestAdapter(ArrayList<YourRequestModel> requestData, Context context) {
        this.requestData = requestData;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcvitem_your_request,parent,false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        holder.txvBloodGroup.setText(requestData.get(position).getBloodGroup());
        holder.txvTime.setText(requestData.get(position).getTime());
     }

    @Override
    public int getItemCount() {
        return requestData.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txvBloodGroup,txvTime;

        public RequestViewHolder(@NonNull View itemView){
            super(itemView);
            txvBloodGroup = itemView.findViewById(R.id.txvBloodGroup);
            txvTime = itemView.findViewById(R.id.txvTime);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            YourRequestModel yourRequestModel = requestData.get(position);
            String requestId=yourRequestModel.getRequestId();

            Intent intent;
            if (yourRequestModel.isAccepted()) {
                intent = new Intent(context, RequestAcceptedReceiverActivity.class);

            } else {
                intent = new Intent(context, WaitingToAcceptActivity.class);
            }
            intent.putExtra("requestId",requestId);
            context.startActivity(intent);
        }
    }
}

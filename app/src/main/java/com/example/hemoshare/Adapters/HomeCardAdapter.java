package com.example.hemoshare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.hemoshare.Models.HomeCardModel;
import com.example.hemoshare.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeCardAdapter extends PagerAdapter {

    List<HomeCardModel> cardList;
    LayoutInflater layoutInflater;
    Context context;

    StorageReference storageRef;

    public HomeCardAdapter(List<HomeCardModel> cardList, Context context) {
        this.cardList = cardList;
        this.context = context;
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public int getCount() {
        return cardList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater =LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_home_card,container,false);

        ImageView imgvCard;
        TextView txvMessage;

        imgvCard = view.findViewById(R.id.imgvCard);
        txvMessage = view.findViewById(R.id.txvMessage);
        txvMessage.setText(cardList.get(position).getMessage());
        Picasso.get().load(cardList.get(position).getCardImgUri()).into(imgvCard);

        /*StorageReference fileRef = storageRef.child(cardList.get(position).getCardImgUri());
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imgvCard);
            }
        });*/
        container.addView(view,0);

        return view;
}

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

package com.panchmukhi.eclinic.Registration.SignUp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.panchmukhi.eclinic.R;

import java.util.List;

public  class MainSpinnerAdapter extends ArrayAdapter<SpinnerParentItemModel> {

    private Context context;
    private int resourceId;
    private List<SpinnerParentItemModel> mainItemList;

    public MainSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<SpinnerParentItemModel> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resourceId = resource;
        this.mainItemList = objects;
    }

    @Override
    public int getCount() {
        return mainItemList.size();
    }

    @Nullable
    @Override
    public SpinnerParentItemModel getItem(int position) {
        return mainItemList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        try {
            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(resourceId, parent, false);

                SpinnerParentItemModel mainItemModel = mainItemList.get(position);
                if (mainItemModel != null) {
                    TextView textView = view.findViewById(R.id.txt);
                    ImageView imageView = view.findViewById(R.id.img);

                    textView.setText(String.valueOf(mainItemModel.getName()));
                    Glide.with(context)
                            .load(mainItemModel.getImage())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(false)
                            .into(imageView);
//                        imageView.setImageResource(mainItemModel.getImage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        try {
            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(resourceId, parent, false);

                SpinnerParentItemModel mainItemModel = mainItemList.get(position);
                if (mainItemModel != null) {
                    TextView textView = view.findViewById(R.id.txt);
                    ImageView imageView = view.findViewById(R.id.img);

                    textView.setText(String.valueOf(mainItemModel.getName()));
                    Glide.with(context)
                            .load(mainItemModel.getImage())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(false)
                            .into(imageView);
//                        imageView.setImageResource(mainItemModel.getImage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
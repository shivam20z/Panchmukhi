package com.panchmukhi.eclinic.Agent.Homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.panchmukhi.eclinic.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private List<SliderItem> mSliderItems;

    public SliderAdapterExample(Context context, List<SliderItem> sliderItems) {
        this.context = context;
        this.mSliderItems = sliderItems;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        SliderItem sliderItem = mSliderItems.get(position);

        viewHolder.tvName.setText(sliderItem.getName());
        viewHolder.tvSpecialist.setText(sliderItem.getSpecialList());

        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImageUrl())
                .fitCenter()
                .into(viewHolder.civDoctorImage);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, String.valueOf(sliderItem.getSpecialList()), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

      static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView civDoctorImage;
        TextView tvName,tvSpecialist;

        public SliderAdapterVH(View itemView) {
            super(itemView);

            civDoctorImage=itemView.findViewById(R.id.civDoctorImage);
            tvName=itemView.findViewById(R.id.tvName);
            tvSpecialist=itemView.findViewById(R.id.tvSpecialist);

            this.itemView = itemView;
        }
    }

}
package com.panchmukhi.eclinic.Agent.Homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.panchmukhi.eclinic.R;

import java.util.List;

public class CommonSpecialistAdapter extends RecyclerView.Adapter<CommonSpecialistAdapter.viewHolder> {

    Context context;
    List<CommonSpecialistModel> list;

    public CommonSpecialistAdapter(Context context, List<CommonSpecialistModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.common_specialist_view, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonSpecialistAdapter.viewHolder holder, int position) {

        holder.setData(list.get(position).getImage(), list.get(position).getName(),list.get(position).getHindiName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvText,tvText2;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tvText = itemView.findViewById(R.id.tvText);
            tvText2 = itemView.findViewById(R.id.tvText2);
            ivImage = itemView.findViewById(R.id.ivImage);
        }

        void setData(int img, String text,String text2) {

            Glide.with(context).load(img).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(ivImage);
            tvText.setText(String.valueOf(text));
            tvText2.setText(String.valueOf(text2));
        }
    }
}

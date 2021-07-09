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

public class CommonSymptomsAdapter extends RecyclerView.Adapter<CommonSymptomsAdapter.viewHolder> {

    Context context;
    List<CommonSymptomsModel> list;
    final int limit = 10;

    public CommonSymptomsAdapter(Context context, List<CommonSymptomsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.common_symptom_view, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.setData(list.get(position).getIvImage(), list.get(position).getTvName(),list.get(position).getTvHindiName());
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

            ivImage = itemView.findViewById(R.id.ivImage);
            tvText = itemView.findViewById(R.id.tvText);
            tvText2 = itemView.findViewById(R.id.tvText2);
        }

        void setData(int img, String txt,String hindiName) {

            Glide.with(context).load(img).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(ivImage);
            tvText.setText(String.valueOf(txt));
            tvText2.setText(String.valueOf(hindiName));
        }
    }
}

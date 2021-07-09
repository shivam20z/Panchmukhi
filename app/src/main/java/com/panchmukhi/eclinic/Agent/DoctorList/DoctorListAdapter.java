package com.panchmukhi.eclinic.Agent.DoctorList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.panchmukhi.eclinic.Agent.BookAppointment.OtpVerification;
import com.panchmukhi.eclinic.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.viewHolder> {

    ArrayList<DoctorListModel> list;
    Context context;

    public DoctorListAdapter(ArrayList<DoctorListModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.all_doctor_view, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.setData(list.get(position).getName(), list.get(position).getSpecialist(), list.get(position).getDegree(), list.get(position).getImageUrl(), list.get(position).getOnlineStatus());

        holder.bookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, OtpVerification.class);
                intent.putExtra("doctorUid",list.get(position).getUid());
                intent.putExtra("userNotificationToken",list.get(position).getUserNotificationToken());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


//    public void filterList(ArrayList<DoctorListModel> filterList) {
//        list = filterList;
//        notifyDataSetChanged();
//    }

    class viewHolder extends RecyclerView.ViewHolder {

        ImageView ivOnlineStatusImg;
        TextView tvOnlineStatus, tvDoctorName, tvDoctorSpecialist, tvDoctorDegree;
        Button bookAppointment;
        CircleImageView civDoctorImage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ivOnlineStatusImg = itemView.findViewById(R.id.ivOnlineStatusImg);
            tvOnlineStatus = itemView.findViewById(R.id.tvOnlineStatus);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvDoctorSpecialist = itemView.findViewById(R.id.tvDoctorSpecialist);
            tvDoctorDegree = itemView.findViewById(R.id.tvDoctorDegree);
            bookAppointment = itemView.findViewById(R.id.bookAppointment);
            civDoctorImage = itemView.findViewById(R.id.civDoctorImage);
        }

        void setData(String doctorName, String specialist, String degree, String img, int onlineStatus) {

            Glide.with(context).load(img).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(civDoctorImage);
            tvDoctorName.setText(String.valueOf(doctorName));
            tvDoctorSpecialist.setText(String.valueOf(specialist));
            tvDoctorDegree.setText(String.valueOf(degree));
            if (onlineStatus == 1) {
                ivOnlineStatusImg.setBackground(context.getDrawable(R.drawable.ic_online));
                tvOnlineStatus.setText(String.valueOf("Online"));
                tvOnlineStatus.setTextColor(context.getResources().getColor(R.color.green_dark));
                bookAppointment.setVisibility(View.VISIBLE);
            } else {
                ivOnlineStatusImg.setBackground(context.getDrawable(R.drawable.ic_offline));
                tvOnlineStatus.setText(String.valueOf("Offline"));
                tvOnlineStatus.setTextColor(context.getResources().getColor(R.color.red1));
                bookAppointment.setVisibility(View.GONE);
            }
        }
    }
}

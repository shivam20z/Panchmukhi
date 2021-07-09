package com.panchmukhi.eclinic.Doctor.DoctorDashBoard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.panchmukhi.eclinic.Agent.AllAppointment.AppointmentListModel;
import com.panchmukhi.eclinic.Doctor.ExploreAppointment.ExploreAppointmentDetails;
import com.panchmukhi.eclinic.R;

import java.util.List;

public class AppointmentListDoctorAdapter extends RecyclerView.Adapter<AppointmentListDoctorAdapter.viewHolder> {

    List<AppointmentListModel> list;
    Context context;

    public AppointmentListDoctorAdapter(List<AppointmentListModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.appointment_orderlist_view,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.setData(list.get(position).getOrderNo(),list.get(position).getName(),list.get(position).getAge(),list.get(position).getCity());

      holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Intent intent=new Intent(context, ExploreAppointmentDetails.class);
              intent.putExtra("doctorOrderNumber",list.get(position).getDoctorOrderNo());
              intent.putExtra("appointmentDate",list.get(position).getAppointmentDate());
              intent.putExtra("appointmentTime",list.get(position).getAppointmentTime());
              intent.putExtra("patientName",list.get(position).getName());
              intent.putExtra("patientAge",list.get(position).getAge());
              intent.putExtra("patientGender",list.get(position).getGender());
              intent.putExtra("Locality",list.get(position).getLocality());
              intent.putExtra("Description",list.get(position).getDescription());
              context.startActivity(intent);
          }
      });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{

        TextView orderNumber,etName,etAge,etCity;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            orderNumber=itemView.findViewById(R.id.orderNumber);
            etName=itemView.findViewById(R.id.etName);
            etAge=itemView.findViewById(R.id.etAge);
            etCity=itemView.findViewById(R.id.etCity);
        }

        void setData(int orderNo,String name,String age,String city){

            orderNumber.setText(String.valueOf("Appointment No :- "+orderNo));
            etName.setText(String.valueOf(name));
            etAge.setText(String.valueOf(age));
            etCity.setText(String.valueOf(city));
        }
    }
}

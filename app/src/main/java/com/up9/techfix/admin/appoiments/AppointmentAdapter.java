package com.up9.techfix.admin.appoiments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.up9.techfix.R;

import java.util.List;
import java.util.Locale;

public class AppointmentAdapter
        extends RecyclerView.Adapter<
        AppointmentAdapter.AppointmentViewHolder> {

    private final List<RepairAppointment>
            appointmentList;

    private final OnAppointmentActionListener
            listener;

    public interface OnAppointmentActionListener {

        void onManage(
                RepairAppointment appointment
        );
    }

    public AppointmentAdapter(
            List<RepairAppointment> appointmentList,
            OnAppointmentActionListener listener
    ) {

        this.appointmentList =
                appointmentList;

        this.listener =
                listener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.item_appointment,
                                parent,
                                false
                        );

        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull AppointmentViewHolder holder,
            int position
    ) {

        RepairAppointment appointment =
                appointmentList.get(position);

        holder.txtAppointmentId.setText(
                "Appointment #"
                        + appointment.getId()
        );

        holder.txtCustomerName.setText(
                "Customer: "
                        + safeText(
                        appointment.getCustomerName()
                )
        );

        holder.txtDevice.setText(
                "Device: "
                        + safeText(
                        appointment.getDevice()
                )
        );

        holder.txtService.setText(
                "Service: "
                        + safeText(
                        appointment.getService()
                )
        );

        holder.txtAppointmentDate.setText(
                "Date: "
                        + safeText(
                        appointment.getAppointmentDate()
                )
        );

        holder.txtAppointmentTime.setText(
                "Time: "
                        + safeText(
                        appointment.getAppointmentTime()
                )
        );

        holder.txtAppointmentBranch.setText(
                "Branch: "
                        + safeText(
                        appointment.getBranch()
                )
        );

        holder.txtAppointmentTechnician.setText(
                "Technician: "
                        + safeText(
                        appointment.getTechnician()
                )
        );

        holder.txtAppointmentPrice.setText(
                String.format(
                        Locale.getDefault(),
                        "Estimated Price: Rs. %,.2f",
                        appointment.getEstimatedPrice()
                )
        );

        holder.txtAppointmentStatus.setText(
                "Status: "
                        + safeText(
                        appointment.getStatus()
                )
        );

        holder.btnManageAppointment
                .setOnClickListener(v -> {

                    if (listener != null) {

                        listener.onManage(
                                appointment
                        );
                    }
                });
    }

    private String safeText(
            String value
    ) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "Not available";
        }

        return value;
    }

    @Override
    public int getItemCount() {

        return appointmentList == null
                ? 0
                : appointmentList.size();
    }

    public static class AppointmentViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtAppointmentId;
        TextView txtCustomerName;
        TextView txtDevice;
        TextView txtService;
        TextView txtAppointmentDate;
        TextView txtAppointmentTime;
        TextView txtAppointmentBranch;
        TextView txtAppointmentTechnician;
        TextView txtAppointmentPrice;
        TextView txtAppointmentStatus;

        Button btnManageAppointment;

        public AppointmentViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            txtAppointmentId =
                    itemView.findViewById(
                            R.id.txtAppointmentId
                    );

            txtCustomerName =
                    itemView.findViewById(
                            R.id.txtCustomerName
                    );

            txtDevice =
                    itemView.findViewById(
                            R.id.txtDevice
                    );

            txtService =
                    itemView.findViewById(
                            R.id.txtService
                    );

            txtAppointmentDate =
                    itemView.findViewById(
                            R.id.txtAppointmentDate
                    );

            txtAppointmentTime =
                    itemView.findViewById(
                            R.id.txtAppointmentTime
                    );

            txtAppointmentBranch =
                    itemView.findViewById(
                            R.id.txtAppointmentBranch
                    );

            txtAppointmentTechnician =
                    itemView.findViewById(
                            R.id.txtAppointmentTechnician
                    );

            txtAppointmentPrice =
                    itemView.findViewById(
                            R.id.txtAppointmentPrice
                    );

            txtAppointmentStatus =
                    itemView.findViewById(
                            R.id.txtAppointmentStatus
                    );

            btnManageAppointment =
                    itemView.findViewById(
                            R.id.btnManageAppointment
                    );
        }
    }
}
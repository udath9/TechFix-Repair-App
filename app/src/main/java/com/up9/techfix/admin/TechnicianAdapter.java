package com.up9.techfix.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.up9.techfix.R;

import java.util.List;

public class TechnicianAdapter
        extends RecyclerView.Adapter<TechnicianAdapter.TechnicianViewHolder> {

    private final List<Technician> technicianList;

    private final OnTechnicianActionListener listener;

    public interface OnTechnicianActionListener {

        void onEdit(Technician technician);

        void onDelete(
                Technician technician,
                int position
        );
    }

    public TechnicianAdapter(
            List<Technician> technicianList,
            OnTechnicianActionListener listener
    ) {
        this.technicianList = technicianList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TechnicianViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.item_technician,
                                parent,
                                false
                        );

        return new TechnicianViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull TechnicianViewHolder holder,
            int position
    ) {

        Technician technician =
                technicianList.get(position);

        holder.txtTechnicianName.setText(
                technician.getName()
        );

        holder.txtTechnicianSpecialization.setText(
                technician.getSpecialization()
        );

        holder.txtTechnicianBranch.setText(
                "Branch: " + technician.getBranch()
        );

        holder.txtTechnicianPhone.setText(
                "Phone: " + technician.getPhone()
        );

        holder.txtTechnicianEmail.setText(
                "Email: " + technician.getEmail()
        );

        if (technician.isAvailable()) {

            holder.txtTechnicianAvailability.setText(
                    "Available"
            );

        } else {

            holder.txtTechnicianAvailability.setText(
                    "Unavailable"
            );
        }

        holder.btnEditTechnician.setOnClickListener(
                v -> listener.onEdit(technician)
        );

        holder.btnDeleteTechnician.setOnClickListener(
                v -> listener.onDelete(
                        technician,
                        holder.getAdapterPosition()
                )
        );
    }

    @Override
    public int getItemCount() {
        return technicianList.size();
    }

    public static class TechnicianViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtTechnicianName;
        TextView txtTechnicianSpecialization;
        TextView txtTechnicianBranch;
        TextView txtTechnicianPhone;
        TextView txtTechnicianEmail;
        TextView txtTechnicianAvailability;

        Button btnEditTechnician;
        Button btnDeleteTechnician;

        public TechnicianViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            txtTechnicianName =
                    itemView.findViewById(
                            R.id.txtTechnicianName
                    );

            txtTechnicianSpecialization =
                    itemView.findViewById(
                            R.id.txtTechnicianSpecialization
                    );

            txtTechnicianBranch =
                    itemView.findViewById(
                            R.id.txtTechnicianBranch
                    );

            txtTechnicianPhone =
                    itemView.findViewById(
                            R.id.txtTechnicianPhone
                    );

            txtTechnicianEmail =
                    itemView.findViewById(
                            R.id.txtTechnicianEmail
                    );

            txtTechnicianAvailability =
                    itemView.findViewById(
                            R.id.txtTechnicianAvailability
                    );

            btnEditTechnician =
                    itemView.findViewById(
                            R.id.btnEditTechnician
                    );

            btnDeleteTechnician =
                    itemView.findViewById(
                            R.id.btnDeleteTechnician
                    );
        }
    }
}

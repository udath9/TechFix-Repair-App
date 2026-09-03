package com.up9.techfix.admin.services;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.up9.techfix.R;

import java.util.List;
import java.util.Locale;

public class ServiceAdapter
        extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private final List<RepairService> serviceList;
    private final OnServiceActionListener listener;

    public interface OnServiceActionListener {

        void onEdit(RepairService service);

        void onDelete(
                RepairService service,
                int position
        );
    }

    public ServiceAdapter(
            List<RepairService> serviceList,
            OnServiceActionListener listener
    ) {
        this.serviceList = serviceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_service,
                        parent,
                        false
                );

        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ServiceViewHolder holder,
            int position
    ) {

        RepairService service = serviceList.get(position);

        holder.txtServiceName.setText(
                service.getName()
        );

        holder.txtServiceDescription.setText(
                service.getDescription()
        );

        holder.txtServicePrice.setText(
                String.format(
                        Locale.getDefault(),
                        "Price: Rs. %.2f",
                        service.getPrice()
                )
        );

        holder.txtServiceDays.setText(
                String.format(
                        Locale.getDefault(),
                        "Estimated Days: %d",
                        service.getEstimatedDays()
                )
        );

        // Display service image
        String imageUri = service.getImageUri();

        if (imageUri != null && !imageUri.isEmpty()) {

            try {

                holder.imgService.setImageURI(
                        Uri.parse(imageUri)
                );

            } catch (Exception e) {

                holder.imgService.setImageResource(
                        android.R.drawable.ic_menu_gallery
                );
            }

        } else {

            holder.imgService.setImageResource(
                    android.R.drawable.ic_menu_gallery
            );
        }

        holder.btnEditService.setOnClickListener(
                v -> listener.onEdit(service)
        );

        holder.btnDeleteService.setOnClickListener(
                v -> {

                    int adapterPosition =
                            holder.getBindingAdapterPosition();

                    if (adapterPosition != RecyclerView.NO_POSITION) {

                        listener.onDelete(
                                service,
                                adapterPosition
                        );
                    }
                }
        );
    }

    @Override
    public int getItemCount() {

        return serviceList.size();
    }

    public static class ServiceViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgService;

        TextView txtServiceName;
        TextView txtServiceDescription;
        TextView txtServicePrice;
        TextView txtServiceDays;

        Button btnEditService;
        Button btnDeleteService;

        public ServiceViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            imgService =
                    itemView.findViewById(
                            R.id.imgService
                    );

            txtServiceName =
                    itemView.findViewById(
                            R.id.txtServiceName
                    );

            txtServiceDescription =
                    itemView.findViewById(
                            R.id.txtServiceDescription
                    );

            txtServicePrice =
                    itemView.findViewById(
                            R.id.txtServicePrice
                    );

            txtServiceDays =
                    itemView.findViewById(
                            R.id.txtServiceDays
                    );

            btnEditService =
                    itemView.findViewById(
                            R.id.btnEditService
                    );

            btnDeleteService =
                    itemView.findViewById(
                            R.id.btnDeleteService
                    );
        }
    }
}
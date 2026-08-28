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

        View view =
                LayoutInflater
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

        RepairService service =
                serviceList.get(position);

        holder.txtServiceName.setText(
                service.getName()
        );

        holder.txtServiceCategory.setText(
                service.getCategory()
        );

        holder.txtServiceDescription.setText(
                service.getDescription()
        );

        holder.txtServicePrice.setText(
                String.format(
                        "Price: Rs. %.2f",
                        service.getPrice()
                )
        );

        holder.txtEstimatedDays.setText(
                "Estimated repair time: "
                        + service.getEstimatedDays()
                        + " day(s)"
        );

        holder.btnEditService.setOnClickListener(
                v -> listener.onEdit(service)
        );

        holder.btnDeleteService.setOnClickListener(
                v -> listener.onDelete(
                        service,
                        holder.getAdapterPosition()
                )
        );
    }

    @Override
    public int getItemCount() {

        return serviceList.size();
    }

    public static class ServiceViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtServiceName;
        TextView txtServiceCategory;
        TextView txtServiceDescription;
        TextView txtServicePrice;
        TextView txtEstimatedDays;

        Button btnEditService;
        Button btnDeleteService;

        public ServiceViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            txtServiceName =
                    itemView.findViewById(
                            R.id.txtServiceName
                    );

            txtServiceCategory =
                    itemView.findViewById(
                            R.id.txtServiceCategory
                    );

            txtServiceDescription =
                    itemView.findViewById(
                            R.id.txtServiceDescription
                    );

            txtServicePrice =
                    itemView.findViewById(
                            R.id.txtServicePrice
                    );

            txtEstimatedDays =
                    itemView.findViewById(
                            R.id.txtEstimatedDays
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
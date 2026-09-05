package com.up9.techfix.Technician;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.up9.techfix.R;

public class TechnicianRepairAdapter
        extends RecyclerView.Adapter<
        TechnicianRepairAdapter.RepairViewHolder> {

    public interface OnRepairClickListener {
        void onRepairClick(int repairId);
    }

    private final Context context;
    private final Cursor cursor;
    private final OnRepairClickListener listener;

    public TechnicianRepairAdapter(
            Context context,
            Cursor cursor,
            OnRepairClickListener listener
    ) {

        this.context = context;
        this.cursor = cursor;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RepairViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.item_technician_repair,
                                parent,
                                false
                        );

        return new RepairViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RepairViewHolder holder,
            int position
    ) {

        if (cursor == null ||
                !cursor.moveToPosition(position)) {

            return;
        }

        int repairId =
                cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                "repair_id"
                        )
                );

        String customerName =
                getString(
                        cursor,
                        "customer_name"
                );

        String deviceModel =
                getString(
                        cursor,
                        "device_model"
                );

        String serviceName =
                getString(
                        cursor,
                        "service_name"
                );

        String branchName =
                getString(
                        cursor,
                        "branch_name"
                );

        String status =
                getString(
                        cursor,
                        "status"
                );

        holder.txtRepairId.setText(
                "Repair #" + repairId
        );

        holder.txtCustomerName.setText(
                customerName
        );

        holder.txtDeviceModel.setText(
                deviceModel
        );

        holder.txtServiceName.setText(
                serviceName
        );

        holder.txtBranchName.setText(
                branchName
        );

        holder.txtStatus.setText(
                status
        );

        holder.itemView.setOnClickListener(
                v -> listener.onRepairClick(
                        repairId
                )
        );
    }

    private String getString(
            Cursor cursor,
            String columnName
    ) {

        int index =
                cursor.getColumnIndex(
                        columnName
                );

        if (index == -1 ||
                cursor.isNull(index)) {

            return "Not available";
        }

        String value =
                cursor.getString(index);

        if (value == null ||
                value.trim().isEmpty()) {

            return "Not available";
        }

        return value;
    }

    @Override
    public int getItemCount() {

        if (cursor == null ||
                cursor.isClosed()) {

            return 0;
        }

        return cursor.getCount();
    }

    public static class RepairViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtRepairId;
        TextView txtCustomerName;
        TextView txtDeviceModel;
        TextView txtServiceName;
        TextView txtBranchName;
        TextView txtStatus;

        public RepairViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            txtRepairId =
                    itemView.findViewById(
                            R.id.txtRepairId
                    );

            txtCustomerName =
                    itemView.findViewById(
                            R.id.txtCustomerName
                    );

            txtDeviceModel =
                    itemView.findViewById(
                            R.id.txtDeviceModel
                    );

            txtServiceName =
                    itemView.findViewById(
                            R.id.txtServiceName
                    );

            txtBranchName =
                    itemView.findViewById(
                            R.id.txtBranchName
                    );

            txtStatus =
                    itemView.findViewById(
                            R.id.txtStatus
                    );
        }
    }
}
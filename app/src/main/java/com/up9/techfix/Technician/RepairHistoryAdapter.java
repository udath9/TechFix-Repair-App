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

public class RepairHistoryAdapter
        extends RecyclerView.Adapter<RepairHistoryAdapter.HistoryViewHolder> {

    private final Context context;
    private final Cursor cursor;

    public RepairHistoryAdapter(
            Context context,
            Cursor cursor
    ) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(context).inflate(
                R.layout.item_repair_history,
                parent,
                false
        );

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull HistoryViewHolder holder,
            int position
    ) {

        if (!cursor.moveToPosition(position)) {
            return;
        }

        int repairId = getInt(
                cursor,
                "repair_id"
        );

        String deviceModel = getString(
                cursor,
                "device_model"
        );

        String serviceName = getString(
                cursor,
                "service_name"
        );

        String status = getString(
                cursor,
                "status"
        );

        String notes = getString(
                cursor,
                "notes"
        );

        String sparePart = getString(
                cursor,
                "spare_part"
        );

        int quantity = getInt(
                cursor,
                "quantity"
        );

        String updateDate = getString(
                cursor,
                "update_date"
        );

        holder.txtHistoryRepairId.setText(
                "Repair #" + repairId
        );

        holder.txtHistoryDevice.setText(
                deviceModel
        );

        holder.txtHistoryService.setText(
                "Service: " + serviceName
        );

        holder.txtHistoryStatus.setText(
                "Status: " + status
        );

        holder.txtHistoryNotes.setText(
                "Notes: " + notes
        );

        holder.txtHistorySparePart.setText(
                "Spare Part: " + sparePart
        );

        holder.txtHistoryQuantity.setText(
                "Quantity: " + quantity
        );

        holder.txtHistoryDate.setText(
                "Updated: " + updateDate
        );
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    private String getString(
            Cursor cursor,
            String column
    ) {

        int index = cursor.getColumnIndex(column);

        if (index == -1 || cursor.isNull(index)) {
            return "Not available";
        }

        String value = cursor.getString(index);

        if (value == null || value.trim().isEmpty()) {
            return "Not available";
        }

        return value;
    }

    private int getInt(
            Cursor cursor,
            String column
    ) {

        int index = cursor.getColumnIndex(column);

        if (index == -1 || cursor.isNull(index)) {
            return 0;
        }

        return cursor.getInt(index);
    }

    static class HistoryViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtHistoryRepairId;
        TextView txtHistoryDevice;
        TextView txtHistoryService;
        TextView txtHistoryStatus;
        TextView txtHistoryNotes;
        TextView txtHistorySparePart;
        TextView txtHistoryQuantity;
        TextView txtHistoryDate;

        public HistoryViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);

            txtHistoryRepairId =
                    itemView.findViewById(
                            R.id.txtHistoryRepairId
                    );

            txtHistoryDevice =
                    itemView.findViewById(
                            R.id.txtHistoryDevice
                    );

            txtHistoryService =
                    itemView.findViewById(
                            R.id.txtHistoryService
                    );

            txtHistoryStatus =
                    itemView.findViewById(
                            R.id.txtHistoryStatus
                    );

            txtHistoryNotes =
                    itemView.findViewById(
                            R.id.txtHistoryNotes
                    );

            txtHistorySparePart =
                    itemView.findViewById(
                            R.id.txtHistorySparePart
                    );

            txtHistoryQuantity =
                    itemView.findViewById(
                            R.id.txtHistoryQuantity
                    );

            txtHistoryDate =
                    itemView.findViewById(
                            R.id.txtHistoryDate
                    );
        }
    }
}
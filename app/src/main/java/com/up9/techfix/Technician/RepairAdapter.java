package com.up9.techfix.Technician;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.up9.techfix.R;

import java.util.List;

public class RepairAdapter extends RecyclerView.Adapter<RepairAdapter.RepairViewHolder> {

    private List<Repair> repairList;

    public RepairAdapter(List<Repair> repairList) {
        this.repairList = repairList;
    }

    @NonNull
    @Override
    public RepairViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_repair, parent, false);

        return new RepairViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepairViewHolder holder, int position) {

        Repair repair = repairList.get(position);

        holder.txtRepairId.setText("Repair #" + repair.getId());

        holder.txtDevice.setText(
                repair.getDeviceCategory() + " - " + repair.getDeviceModel()
        );

        holder.txtService.setText(repair.getServiceName());

        holder.txtProblem.setText(
                "Problem: " + repair.getProblemDescription()
        );

        holder.txtStatus.setText(
                "Status: " + repair.getStatus()
        );

        holder.txtRepairDate.setText(
                "Date: " + repair.getRepairDate()
        );
    }

    @Override
    public int getItemCount() {
        return repairList.size();
    }

    public static class RepairViewHolder extends RecyclerView.ViewHolder {

        TextView txtRepairId;
        TextView txtDevice;
        TextView txtService;
        TextView txtProblem;
        TextView txtStatus;
        TextView txtRepairDate;

        public RepairViewHolder(@NonNull View itemView) {
            super(itemView);

            txtRepairId = itemView.findViewById(R.id.txtRepairId);
            txtDevice = itemView.findViewById(R.id.txtDevice);
            txtService = itemView.findViewById(R.id.txtService);
            txtProblem = itemView.findViewById(R.id.txtProblem);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtRepairDate = itemView.findViewById(R.id.txtRepairDate);
        }
    }
}
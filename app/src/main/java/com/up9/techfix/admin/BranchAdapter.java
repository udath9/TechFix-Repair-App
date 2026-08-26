package com.up9.techfix.admin;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.up9.techfix.R;

import java.util.List;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder> {

    private final List<Branch> branchList;
    private final OnBranchActionListener listener;

    public interface OnBranchActionListener {
        void onEdit(Branch branch);
        void onDelete(Branch branch, int position);
    }

    public BranchAdapter(
            List<Branch> branchList,
            OnBranchActionListener listener
    ) {

        this.branchList = branchList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_branch, parent, false);

        return new BranchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull BranchViewHolder holder,
            int position
    ) {

        Branch branch = branchList.get(position);

        holder.txtBranchName.setText(branch.getName());

        holder.txtBranchAddress.setText(
                branch.getAddress()
        );

        holder.txtBranchPhone.setText(
                branch.getPhone()
        );

        holder.txtBranchLocation.setText(
                branch.getLatitude()
                        + ", "
                        + branch.getLongitude()
        );

        holder.btnEdit.setOnClickListener(v -> {

            listener.onEdit(branch);

        });

        holder.btnDelete.setOnClickListener(v -> {

            listener.onDelete(
                    branch,
                    holder.getAdapterPosition()
            );

        });
    }

    @Override
    public int getItemCount() {

        return branchList.size();
    }

    public static class BranchViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtBranchName;
        TextView txtBranchAddress;
        TextView txtBranchPhone;
        TextView txtBranchLocation;

        Button btnEdit;
        Button btnDelete;

        public BranchViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            txtBranchName =
                    itemView.findViewById(
                            R.id.txtBranchName
                    );

            txtBranchAddress =
                    itemView.findViewById(
                            R.id.txtBranchAddress
                    );

            txtBranchPhone =
                    itemView.findViewById(
                            R.id.txtBranchPhone
                    );

            txtBranchLocation =
                    itemView.findViewById(
                            R.id.txtBranchLocation
                    );

            btnEdit =
                    itemView.findViewById(
                            R.id.btnEdit
                    );

            btnDelete =
                    itemView.findViewById(
                            R.id.btnDelete
                    );
        }
    }
}
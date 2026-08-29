package com.up9.techfix.admin;

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

public class RepairSampleAdapter
        extends RecyclerView.Adapter<RepairSampleAdapter.SampleViewHolder> {

    private final List<RepairSample> sampleList;

    private final OnSampleActionListener listener;

    public interface OnSampleActionListener {

        void onEdit(RepairSample sample);

        void onDelete(RepairSample sample);
    }

    public RepairSampleAdapter(
            List<RepairSample> sampleList,
            OnSampleActionListener listener
    ) {
        this.sampleList = sampleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SampleViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.item_repair_sample,
                                parent,
                                false
                        );

        return new SampleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull SampleViewHolder holder,
            int position
    ) {

        RepairSample sample =
                sampleList.get(position);

        holder.imgRepairSample.setImageResource(
                sample.getImageResource()
        );

        holder.txtSampleDevice.setText(
                sample.getDeviceName()
        );

        holder.txtSampleCategory.setText(
                "Category: " +
                        sample.getCategory()
        );

        holder.txtSampleService.setText(
                "Service: " +
                        sample.getService()
        );

        holder.txtSampleDescription.setText(
                sample.getDescription()
        );

        holder.btnEditSample.setOnClickListener(
                v -> listener.onEdit(sample)
        );

        holder.btnDeleteSample.setOnClickListener(
                v -> listener.onDelete(sample)
        );
    }

    @Override
    public int getItemCount() {

        return sampleList.size();
    }

    public static class SampleViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgRepairSample;

        TextView txtSampleDevice;
        TextView txtSampleCategory;
        TextView txtSampleService;
        TextView txtSampleDescription;

        Button btnEditSample;
        Button btnDeleteSample;

        public SampleViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            imgRepairSample =
                    itemView.findViewById(
                            R.id.imgRepairSample
                    );

            txtSampleDevice =
                    itemView.findViewById(
                            R.id.txtSampleDevice
                    );

            txtSampleCategory =
                    itemView.findViewById(
                            R.id.txtSampleCategory
                    );

            txtSampleService =
                    itemView.findViewById(
                            R.id.txtSampleService
                    );

            txtSampleDescription =
                    itemView.findViewById(
                            R.id.txtSampleDescription
                    );

            btnEditSample =
                    itemView.findViewById(
                            R.id.btnEditSample
                    );

            btnDeleteSample =
                    itemView.findViewById(
                            R.id.btnDeleteSample
                    );
        }
    }
}
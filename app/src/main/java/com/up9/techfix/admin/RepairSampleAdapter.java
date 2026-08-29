package com.up9.techfix.admin;

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

public class RepairSampleAdapter
        extends RecyclerView.Adapter<RepairSampleAdapter.RepairSampleViewHolder> {

    private final List<RepairSample> sampleList;

    private final OnRepairSampleActionListener listener;

    public interface OnRepairSampleActionListener {

        void onEdit(RepairSample sample);

        void onDelete(
                RepairSample sample,
                int position
        );
    }

    public RepairSampleAdapter(
            List<RepairSample> sampleList,
            OnRepairSampleActionListener listener
    ) {

        this.sampleList = sampleList;

        this.listener = listener;
    }

    @NonNull
    @Override
    public RepairSampleViewHolder onCreateViewHolder(
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

        return new RepairSampleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RepairSampleViewHolder holder,
            int position
    ) {

        RepairSample sample =
                sampleList.get(position);

        holder.txtSampleDeviceName.setText(
                sample.getDeviceName()
        );

        holder.txtSampleCategory.setText(
                "Category: "
                        + sample.getCategory()
        );

        holder.txtSampleService.setText(
                "Service: "
                        + sample.getService()
        );

        holder.txtSampleDescription.setText(
                sample.getDescription()
        );

        String imageUri =
                sample.getImageUri();

        if (imageUri != null
                && !imageUri.isEmpty()) {

            try {

                Uri uri =
                        Uri.parse(imageUri);

                holder.imgSample.setImageURI(
                        uri
                );

            } catch (Exception e) {

                holder.imgSample.setImageResource(
                        android.R.drawable.ic_menu_gallery
                );
            }

        } else {

            holder.imgSample.setImageResource(
                    android.R.drawable.ic_menu_gallery
            );
        }

        holder.btnEditSample.setOnClickListener(
                v -> listener.onEdit(sample)
        );

        holder.btnDeleteSample.setOnClickListener(
                v -> {

                    int adapterPosition =
                            holder.getAdapterPosition();

                    if (adapterPosition !=
                            RecyclerView.NO_POSITION) {

                        listener.onDelete(
                                sample,
                                adapterPosition
                        );
                    }
                }
        );
    }

    @Override
    public int getItemCount() {

        return sampleList.size();
    }

    public static class RepairSampleViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgSample;

        TextView txtSampleDeviceName;
        TextView txtSampleCategory;
        TextView txtSampleService;
        TextView txtSampleDescription;

        Button btnEditSample;
        Button btnDeleteSample;

        public RepairSampleViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            imgSample =
                    itemView.findViewById(
                            R.id.imgSample
                    );

            txtSampleDeviceName =
                    itemView.findViewById(
                            R.id.txtSampleDeviceName
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
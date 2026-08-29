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

public class SparePartAdapter
        extends RecyclerView.Adapter<SparePartAdapter.SparePartViewHolder> {

    private final List<SparePart> sparePartList;

    private final OnSparePartActionListener listener;

    public interface OnSparePartActionListener {

        void onEdit(SparePart sparePart);

        void onDelete(
                SparePart sparePart,
                int position
        );
    }

    public SparePartAdapter(
            List<SparePart> sparePartList,
            OnSparePartActionListener listener
    ) {

        this.sparePartList = sparePartList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SparePartViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.item_spare_part,
                                parent,
                                false
                        );

        return new SparePartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull SparePartViewHolder holder,
            int position
    ) {

        SparePart part =
                sparePartList.get(position);

        holder.txtPartName.setText(
                part.getName()
        );

        holder.txtPartCode.setText(
                "Part Code: " + part.getPartNumber()
        );

        holder.txtPartCategory.setText(
                "Category: " + part.getCategory()
        );

        holder.txtPartQuantity.setText(
                "Stock: " + part.getQuantity()
        );

        holder.txtPartPrice.setText(
                String.format(
                        "Unit Price: Rs. %.2f",
                        part.getUnitPrice()
                )
        );

        holder.txtPartSupplier.setText(
                "Supplier: " + part.getSupplier()
        );

        if (part.getQuantity() <= 0) {

            holder.txtPartAvailability.setText(
                    "Out of Stock"
            );

        } else if (part.isLowStock()) {

            holder.txtPartAvailability.setText(
                    "Low Stock"
            );

        } else {

            holder.txtPartAvailability.setText(
                    "Available"
            );
        }

        holder.btnEditSparePart.setOnClickListener(
                v -> listener.onEdit(part)
        );

        holder.btnDeleteSparePart.setOnClickListener(
                v -> {

                    int adapterPosition =
                            holder.getAdapterPosition();

                    if (adapterPosition != RecyclerView.NO_POSITION) {

                        listener.onDelete(
                                sparePartList.get(adapterPosition),
                                adapterPosition
                        );
                    }
                }
        );
    }

    @Override
    public int getItemCount() {

        return sparePartList.size();
    }

    public static class SparePartViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtPartName;
        TextView txtPartCode;
        TextView txtPartCategory;
        TextView txtPartQuantity;
        TextView txtPartPrice;
        TextView txtPartSupplier;
        TextView txtPartAvailability;

        Button btnEditSparePart;
        Button btnDeleteSparePart;

        public SparePartViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            txtPartName =
                    itemView.findViewById(
                            R.id.txtPartName
                    );

            txtPartCode =
                    itemView.findViewById(
                            R.id.txtPartCode
                    );

            txtPartCategory =
                    itemView.findViewById(
                            R.id.txtPartCategory
                    );

            txtPartQuantity =
                    itemView.findViewById(
                            R.id.txtPartQuantity
                    );

            txtPartPrice =
                    itemView.findViewById(
                            R.id.txtPartPrice
                    );

            txtPartSupplier =
                    itemView.findViewById(
                            R.id.txtPartSupplier
                    );

            txtPartAvailability =
                    itemView.findViewById(
                            R.id.txtPartAvailability
                    );

            btnEditSparePart =
                    itemView.findViewById(
                            R.id.btnEditSparePart
                    );

            btnDeleteSparePart =
                    itemView.findViewById(
                            R.id.btnDeleteSparePart
                    );
        }
    }
}
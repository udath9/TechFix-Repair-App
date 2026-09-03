package com.up9.techfix.admin.categories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.up9.techfix.R;

import java.util.List;

public class CategoryAdapter
        extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<DeviceCategory> categoryList;

    private final OnCategoryActionListener listener;


    public interface OnCategoryActionListener {

        void onEdit(DeviceCategory category);

        void onDelete(
                DeviceCategory category,
                int position
        );
    }


    public CategoryAdapter(
            List<DeviceCategory> categoryList,
            OnCategoryActionListener listener
    ) {

        this.categoryList = categoryList;

        this.listener = listener;
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.item_category,
                                parent,
                                false
                        );

        return new CategoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(
            @NonNull CategoryViewHolder holder,
            int position
    ) {

        DeviceCategory category =
                categoryList.get(position);


        holder.txtCategoryName.setText(
                category.getName()
        );


        holder.txtCategoryDescription.setText(
                category.getDescription()
        );


        holder.txtCategoryPriceModifier.setText(
                "Price Modifier: "
                        + category.getPriceModifier()
                        + "%"
        );


        holder.btnEditCategory.setOnClickListener(
                v -> listener.onEdit(category)
        );


        holder.btnDeleteCategory.setOnClickListener(
                v -> listener.onDelete(
                        category,
                        holder.getAdapterPosition()
                )
        );
    }


    @Override
    public int getItemCount() {

        return categoryList.size();
    }


    public static class CategoryViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtCategoryName;
        TextView txtCategoryDescription;
        TextView txtCategoryPriceModifier;

        Button btnEditCategory;
        Button btnDeleteCategory;


        public CategoryViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);


            txtCategoryName =
                    itemView.findViewById(
                            R.id.txtCategoryName
                    );


            txtCategoryDescription =
                    itemView.findViewById(
                            R.id.txtCategoryDescription
                    );


            txtCategoryPriceModifier =
                    itemView.findViewById(
                            R.id.txtCategoryPriceModifier
                    );


            btnEditCategory =
                    itemView.findViewById(
                            R.id.btnEditCategory
                    );


            btnDeleteCategory =
                    itemView.findViewById(
                            R.id.btnDeleteCategory
                    );
        }
    }
}
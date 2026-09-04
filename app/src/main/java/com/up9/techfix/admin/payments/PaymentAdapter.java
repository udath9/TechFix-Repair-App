package com.up9.techfix.admin.payments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.up9.techfix.R;
import com.up9.techfix.data.Payment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentAdapter
        extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private final List<Payment> paymentList;
    private final OnPaymentActionListener listener;

    public interface OnPaymentActionListener {

        void onManage(Payment payment);
    }

    public PaymentAdapter(
            List<Payment> paymentList,
            OnPaymentActionListener listener
    ) {
        this.paymentList = paymentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.item_payment,
                                parent,
                                false
                        );

        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PaymentViewHolder holder,
            int position
    ) {

        Payment payment =
                paymentList.get(position);



        holder.txtPaymentReference.setText(
                "Payment #" +
                        payment.getId()
        );


        holder.txtPaymentCustomer.setText(
                "Customer: " +
                        safeText(
                                payment.getCustomerName()
                        )
        );


        holder.txtPaymentAppointment.setText(
                "Repair: #" +
                        payment.getRepairId()
        );

        holder.txtPaymentAmount.setText(
                String.format(
                        Locale.getDefault(),
                        "Amount: LKR %,.2f",
                        payment.getAmount()
                )
        );

        holder.txtPaymentMethod.setText(
                "Service: " +
                        safeText(
                                payment.getServiceName()
                        )
        );


        holder.txtPaymentDate.setText(
                "Date: " +
                        formatPaymentDate(
                                payment.getPaymentDate()
                        )
        );


        holder.txtPaymentStatus.setText(
                "Status: " +
                        safeText(
                                payment.getStatus()
                        )
        );

        holder.btnManagePayment.setOnClickListener(
                v -> {

                    if (listener != null) {

                        listener.onManage(
                                payment
                        );
                    }
                }
        );
    }


    private String safeText(
            String value
    ) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "Not available";
        }

        return value;
    }

    private String formatPaymentDate(
            String dateValue
    ) {

        if (dateValue == null ||
                dateValue.trim().isEmpty()) {

            return "Unknown";
        }

        try {

            long timestamp =
                    Long.parseLong(
                            dateValue
                    );

            SimpleDateFormat formatter =
                    new SimpleDateFormat(
                            "dd MMMM yyyy",
                            Locale.getDefault()
                    );

            return formatter.format(
                    new Date(timestamp)
            );

        } catch (Exception e) {

            return dateValue;
        }
    }

    @Override
    public int getItemCount() {

        return paymentList == null
                ? 0
                : paymentList.size();
    }

    public static class PaymentViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtPaymentReference;
        TextView txtPaymentCustomer;
        TextView txtPaymentAppointment;
        TextView txtPaymentAmount;
        TextView txtPaymentMethod;
        TextView txtPaymentDate;
        TextView txtPaymentStatus;

        Button btnManagePayment;

        public PaymentViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            txtPaymentReference =
                    itemView.findViewById(
                            R.id.txtPaymentReference
                    );

            txtPaymentCustomer =
                    itemView.findViewById(
                            R.id.txtPaymentCustomer
                    );

            txtPaymentAppointment =
                    itemView.findViewById(
                            R.id.txtPaymentAppointment
                    );

            txtPaymentAmount =
                    itemView.findViewById(
                            R.id.txtPaymentAmount
                    );

            txtPaymentMethod =
                    itemView.findViewById(
                            R.id.txtPaymentMethod
                    );

            txtPaymentDate =
                    itemView.findViewById(
                            R.id.txtPaymentDate
                    );

            txtPaymentStatus =
                    itemView.findViewById(
                            R.id.txtPaymentStatus
                    );

            btnManagePayment =
                    itemView.findViewById(
                            R.id.btnManagePayment
                    );
        }
    }
}
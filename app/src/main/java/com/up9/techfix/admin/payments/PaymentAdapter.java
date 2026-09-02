package com.up9.techfix.admin.payments;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.up9.techfix.R;

import java.util.List;

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
                        payment.getPaymentReference()
        );

        holder.txtPaymentCustomer.setText(
                "Customer: " +
                        payment.getCustomerName()
        );

        holder.txtPaymentAppointment.setText(
                "Appointment: #" +
                        payment.getAppointmentId()
        );

        holder.txtPaymentAmount.setText(
                String.format(
                        "Amount: Rs. %.2f",
                        payment.getAmount()
                )
        );

        holder.txtPaymentMethod.setText(
                "Method: " +
                        payment.getPaymentMethod()
        );

        holder.txtPaymentDate.setText(
                "Date: " +
                        payment.getPaymentDate()
        );

        holder.txtPaymentStatus.setText(
                payment.getPaymentStatus()
        );

        holder.btnManagePayment.setOnClickListener(
                v -> listener.onManage(payment)
        );
    }

    @Override
    public int getItemCount() {

        return paymentList.size();
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
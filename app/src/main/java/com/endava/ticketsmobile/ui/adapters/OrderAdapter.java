package com.endava.ticketsmobile.ui.adapters;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.endava.ticketsmobile.R;
import com.endava.ticketsmobile.data.model.Order;
import com.endava.ticketsmobile.data.model.OrderPatch;
import com.endava.ticketsmobile.data.model.TicketCategory;
import com.endava.ticketsmobile.data.services.TicketsNetService;
import com.endava.ticketsmobile.data.services.util.TicketsErrorHandler;
import com.endava.ticketsmobile.data.services.util.TicketsServiceFactory;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private final List<Order> orders;

    public OrderAdapter() {
        this.orders = new ArrayList<>();
    }

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new OrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order);

        holder.expandButton.setOnClickListener(view -> {
            boolean expanded = order.isExpanded();

            order.setExpanded(!expanded);
            notifyItemChanged(position);
        });

        holder.orderDeleteButton.setOnClickListener(view -> {
            TicketsNetService ticketsNetService = TicketsServiceFactory.createTicketsServiceForNet();
            Call<String> orderDeleteCall = ticketsNetService.deleteOrder(order.getId());
            orderDeleteCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(holder.itemView.getContext(), "Order deleted!", Toast.LENGTH_SHORT)
                                .show();
                        orders.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    } else {
                        String errorMessage = TicketsErrorHandler.getErrorMessageFromResponse(response);
                        if (errorMessage != null) {
                            Toast.makeText(holder.itemView.getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT)
                                    .show();
                            Log.d("NetworkRequest", errorMessage);
                        } else {
                            Toast.makeText(holder.itemView.getContext(), "Error " + response.message(), Toast.LENGTH_SHORT)
                                    .show();
                            Log.d("NetworkRequest", response.message());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Toast.makeText(holder.itemView.getContext(), "Error " + t.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                    Log.e("NetworkRequest", t.getMessage());
                }
            });
        });

        holder.orderUpdateButton.setOnClickListener(view -> {
            String selectedTicketCategory = holder.ticketCategoriesTextView.getText().toString();
            int index = holder.ticketCategories.indexOf(selectedTicketCategory);

            if (index == -1) {
                holder.orderTicketCategoryField.setError("Select a category");
                return;
            }

            Editable numberOfTicketsEditable = holder.numberTicketsTextEdit.getText();
            if (numberOfTicketsEditable == null) {
                holder.orderNumberTicketsField.setError("Enter a valid number");
                return;
            }

            int numberOfTickets;
            try {
                numberOfTickets = Integer.parseInt(numberOfTicketsEditable.toString());

                if (numberOfTickets == 0) {
                    holder.orderNumberTicketsField.setError("Enter a valid number");
                    return;
                }
            } catch (NumberFormatException ex) {
                holder.orderNumberTicketsField.setError("Enter a valid number");
                return;
            }

            OrderPatch orderPatch = new OrderPatch(order.getEvent().getTicketCategories().get(index).getId(), numberOfTickets);

            TicketsNetService ticketsNetService = TicketsServiceFactory.createTicketsServiceForNet();
            Call<Order> orderUpdateCall = ticketsNetService.updateOrder(order.getId(), orderPatch);
            orderUpdateCall.enqueue(new Callback<Order>() {
                @Override
                public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(holder.itemView.getContext(), "Order updated!", Toast.LENGTH_SHORT)
                                .show();
                        orders.set(holder.getAdapterPosition(), response.body());
                        notifyItemChanged(holder.getAdapterPosition());
                        holder.clearFields();
                    } else {
                        String errorMessage = TicketsErrorHandler.getErrorMessageFromResponse(response);
                        if (errorMessage != null) {
                            Toast.makeText(holder.itemView.getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT)
                                    .show();
                            Log.d("NetworkRequest", errorMessage);
                        } else {
                            Toast.makeText(holder.itemView.getContext(), "Error " + response.message(), Toast.LENGTH_SHORT)
                                    .show();
                            Log.d("NetworkRequest", response.message());
                        }
                        holder.clearFields();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                    Toast.makeText(holder.itemView.getContext(), "Error " + t.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                    Log.e("NetworkRequest", t.getMessage());
                    holder.clearFields();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void updateData(List<Order> orders) {
        int oldItemCount = this.getItemCount();
        this.orders.clear();
        notifyItemRangeRemoved(0, oldItemCount);
        this.orders.addAll(orders);
        notifyItemRangeChanged(0, getItemCount());
    }

    public List<Order> getData() {
        return new ArrayList<>(this.orders);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventNameCategoryField;
        private final TextView quantityField;
        private final TextView totalPriceField;
        private final TextView dateField;
        private final MaterialAutoCompleteTextView ticketCategoriesTextView;
        private final TextInputEditText numberTicketsTextEdit;
        private final ConstraintLayout buyLayout;
        private final MaterialCardView cardView;
        private final Button expandButton;
        private final Button orderDeleteButton;
        private final Button orderUpdateButton;
        private final TextInputLayout orderTicketCategoryField;
        private final TextInputLayout orderNumberTicketsField;

        private final List<String> ticketCategories = new ArrayList<>();


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventNameCategoryField = itemView.findViewById(R.id.eventNameCategoryField);
            quantityField = itemView.findViewById(R.id.quantityField);
            totalPriceField = itemView.findViewById(R.id.totalPriceField);
            dateField = itemView.findViewById(R.id.dateField);
            ticketCategoriesTextView = itemView.findViewById(R.id.orderTicketCategoriesTextView);
            numberTicketsTextEdit = itemView.findViewById(R.id.orderNumberTicketsTextEdit);
            expandButton = itemView.findViewById(R.id.orderExpandButton);
            cardView = itemView.findViewById(R.id.cardViewOrderItem);
            buyLayout = itemView.findViewById(R.id.orderActionsLayout);
            orderDeleteButton = itemView.findViewById(R.id.orderDeleteButton);
            orderUpdateButton = itemView.findViewById(R.id.orderUpdateButton);
            orderTicketCategoryField = itemView.findViewById(R.id.orderTicketCategoryField);
            orderNumberTicketsField = itemView.findViewById(R.id.orderNumberTicketsField);
        }

        public void clearFields() {
            orderTicketCategoryField.setError("");
            orderNumberTicketsField.setError("");
        }

        public void bind(Order order) {
            eventNameCategoryField.setText(String.format("%s - %s", order.getEvent().getName(), order.getTicketCategory().getDescription()));
            quantityField.setText(String.format(Locale.ENGLISH, "Quantity: %d", order.getNumberOfTickets()));
            totalPriceField.setText(String.format(Locale.ENGLISH, "Total Price: %.2f", order.getTotalPrice()));
            dateField.setText(order.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

            ticketCategories.clear();
            String selectedTicketCategory = "";
            for (TicketCategory tc: order.getEvent().getTicketCategories()) {
                String ticketCategoryItem = tc.getDescription() + " - " + tc.getPrice();
                ticketCategories.add(ticketCategoryItem);

                if (tc.getId().equals(order.getTicketCategory().getId())) {
                    selectedTicketCategory = ticketCategoryItem;
                }
            }
            ticketCategoriesTextView.setSimpleItems(ticketCategories.toArray(new String[0]));
            ticketCategoriesTextView.setText(selectedTicketCategory, false);
            numberTicketsTextEdit.setText(String.format(Locale.ENGLISH, "%d", order.getNumberOfTickets()));

            if (order.isExpanded()) {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                buyLayout.setVisibility(View.VISIBLE);
                expandButton.setText(R.string.collapse_btn_text);
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                buyLayout.setVisibility(View.GONE);
                expandButton.setText(R.string.expand_btn_text);
            }
        }
    }
}

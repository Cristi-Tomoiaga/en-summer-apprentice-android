package com.endava.ticketsmobile.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.endava.ticketsmobile.R;
import com.endava.ticketsmobile.data.model.Order;
import com.endava.ticketsmobile.data.model.TicketCategory;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private final List<Order> orders;

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
    }

    @Override
    public int getItemCount() {
        return orders.size();
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
        public final Button expandButton;

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
        }

        public void bind(Order order) {
            eventNameCategoryField.setText(String.format("%s - %s", order.getEvent().getName(), order.getTicketCategory().getDescription()));
            quantityField.setText(String.format(Locale.ENGLISH, "Quantity: %d", order.getNumberOfTickets()));
            totalPriceField.setText(String.format(Locale.ENGLISH, "Total Price: %.2f", order.getTotalPrice()));
            dateField.setText(order.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

            List<String> ticketCategories = new ArrayList<>();
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

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

import com.bumptech.glide.Glide;
import com.endava.ticketsmobile.R;
import com.endava.ticketsmobile.data.model.Event;
import com.endava.ticketsmobile.data.model.Order;
import com.endava.ticketsmobile.data.model.OrderPost;
import com.endava.ticketsmobile.data.model.TicketCategory;
import com.endava.ticketsmobile.data.services.TicketsJavaService;
import com.endava.ticketsmobile.data.services.util.TicketsServiceFactory;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private final List<Event> events;

    public EventAdapter() {
        this.events = new ArrayList<>();
    }

    public EventAdapter(List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event);

        holder.expandButton.setOnClickListener(view -> {
            boolean expanded = event.isExpanded();

            event.setExpanded(!expanded);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void updateData(List<Event> events) {
        this.events.clear();
        this.events.addAll(events);
        notifyItemRangeChanged(0, getItemCount());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView eventImage;
        private final TextView eventName;
        private final TextView eventDescription;
        private final MaterialAutoCompleteTextView ticketCategoryTextView;
        private final ConstraintLayout buyLayout;
        private final MaterialCardView cardView;
        private final Button expandButton;
        private final Button buyButton;
        private final TextInputEditText numberTicketsEditText;
        private final TextInputLayout ticketCategoryField;
        private final TextInputLayout numberTicketsField;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventImage = itemView.findViewById(R.id.eventCardImage);
            eventName = itemView.findViewById(R.id.eventCardName);
            eventDescription = itemView.findViewById(R.id.eventCardDescription);
            ticketCategoryTextView = itemView.findViewById(R.id.ticketCategoriesTextView);
            expandButton = itemView.findViewById(R.id.expandButton);
            buyLayout = itemView.findViewById(R.id.buyLayout);
            cardView = itemView.findViewById(R.id.cardViewItem);
            buyButton = itemView.findViewById(R.id.buyButton);
            numberTicketsEditText = itemView.findViewById(R.id.numberTicketsEditText);
            ticketCategoryField = itemView.findViewById(R.id.ticketCategoryField);
            numberTicketsField = itemView.findViewById(R.id.numberTicketsField);
        }

        public void clearFields() {
            ticketCategoryField.setError("");
            numberTicketsField.setError("");

            ticketCategoryTextView.setText("");
            numberTicketsEditText.setText("");
        }

        public void bind(Event event) {
            eventName.setText(event.getName());
            eventDescription.setText(event.getDescription());

            List<String> ticketCategories = new ArrayList<>();
            for (TicketCategory tc: event.getTicketCategories()) {
                ticketCategories.add(tc.getDescription() + " - " + tc.getPrice());
            }
            ticketCategoryTextView.setSimpleItems(ticketCategories.toArray(new String[0]));

            Glide.with(this.itemView)
                    .load(event.getImage())
                    .centerCrop()
                    .into(eventImage);

            if (event.isExpanded()) {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                buyLayout.setVisibility(View.VISIBLE);
                expandButton.setText(R.string.collapse_btn_text);
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                buyLayout.setVisibility(View.GONE);
                expandButton.setText(R.string.expand_btn_text);
            }

            buyButton.setOnClickListener(view -> {
                String selectedTicketCategory = ticketCategoryTextView.getText().toString();
                int index = ticketCategories.indexOf(selectedTicketCategory);

                if (index == -1) {
                    ticketCategoryField.setError("Select a category");
                    return;
                }

                Editable numberOfTicketsEditable = numberTicketsEditText.getText();
                if (numberOfTicketsEditable == null) {
                    numberTicketsField.setError("Enter a valid number");
                    return;
                }

                int numberOfTickets;
                try {
                    numberOfTickets = Integer.parseInt(numberOfTicketsEditable.toString());

                    if (numberOfTickets == 0) {
                        numberTicketsField.setError("Enter a valid number");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    numberTicketsField.setError("Enter a valid number");
                    return;
                }

                OrderPost orderPost = new OrderPost(event.getId(), event.getTicketCategories().get(index).getId(), numberOfTickets);

                TicketsJavaService ticketsJavaService = TicketsServiceFactory.createTicketServiceForJava();
                Call<Order> orderCreateCall = ticketsJavaService.createOrder(orderPost);
                orderCreateCall.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(itemView.getContext(), "Order saved!", Toast.LENGTH_SHORT)
                                    .show();
                            clearFields();
                        } else {
                            Toast.makeText(itemView.getContext(), "Error " + response.message(), Toast.LENGTH_SHORT)
                                    .show();
                            Log.d("NetworkRequest", response.message());
                            clearFields();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                        Toast.makeText(itemView.getContext(), "Error " + t.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                        Log.e("NetworkRequest", t.getMessage());
                        clearFields();
                    }
                });
            });
        }
    }
}

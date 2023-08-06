package com.endava.ticketsmobile.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.endava.ticketsmobile.R;
import com.endava.ticketsmobile.data.model.Event;
import com.endava.ticketsmobile.data.model.TicketCategory;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private final List<Event> events;

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView eventImage;
        private final TextView eventName;
        private final TextView eventDescription;
        private final MaterialAutoCompleteTextView ticketCategoryTextView;
        private final ConstraintLayout buyLayout;
        private final MaterialCardView cardView;
        private final Button expandButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventImage = itemView.findViewById(R.id.eventCardImage);
            eventName = itemView.findViewById(R.id.eventCardName);
            eventDescription = itemView.findViewById(R.id.eventCardDescription);
            ticketCategoryTextView = itemView.findViewById(R.id.ticketCategoriesTextView);
            expandButton = itemView.findViewById(R.id.expandButton);
            buyLayout = itemView.findViewById(R.id.buyLayout);
            cardView = itemView.findViewById(R.id.cardViewItem);
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
        }
    }
}

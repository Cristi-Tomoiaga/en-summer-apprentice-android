package com.endava.ticketsmobile.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.endava.ticketsmobile.R;
import com.endava.ticketsmobile.ui.activities.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class FilterModalBottomSheet extends BottomSheetDialogFragment {
    public static final String TAG = "FilterModalBottomSheet";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_bottom_sheet, container, false);

        TextInputEditText eventNameEditText = view.findViewById(R.id.eventNameEditText);
        TextInputEditText venueEditText = view.findViewById(R.id.venueEditText);
        TextInputEditText eventTypeEditText = view.findViewById(R.id.eventTypeEditText);

        MainActivity mainActivity = (MainActivity) requireActivity();
        eventNameEditText.setText(mainActivity.getEventNameFilter());
        eventTypeEditText.setText(mainActivity.getEventTypeFilter());
        venueEditText.setText(mainActivity.getVenueFilter());

        Button filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(v -> {
            String eventName = Objects.requireNonNull(eventNameEditText.getText()).toString().trim();
            String venue = Objects.requireNonNull(venueEditText.getText()).toString().trim();
            String eventType = Objects.requireNonNull(eventTypeEditText.getText()).toString().trim();

            mainActivity.filterEvents(eventName, venue, eventType);
            dismiss();
        });

        return view;
    }
}

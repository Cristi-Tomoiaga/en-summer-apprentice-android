package com.endava.ticketsmobile.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.endava.ticketsmobile.R;
import com.endava.ticketsmobile.data.model.Event;
import com.endava.ticketsmobile.data.model.TicketCategory;
import com.endava.ticketsmobile.data.model.Venue;
import com.endava.ticketsmobile.ui.adapters.EventAdapter;
import com.endava.ticketsmobile.ui.fragments.FilterModalBottomSheet;
import com.endava.ticketsmobile.ui.fragments.SortModalBottomSheet;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<Event> getMockEvents() {
        return Arrays.asList(
                new Event(
                        1,
                        new Venue(
                                1,
                                "Cluj",
                                "Venue",
                                1000
                        ),
                        "Concert",
                        "Come and see for yourself. Free candies to everyone :)",
                        "Untold",
                        "https://weraveyou.com/wp-content/uploads/2023/03/UNTOLD.jpeg",
                        LocalDateTime.of(2023, 8, 10, 12, 0),
                        LocalDateTime.of(2023, 8, 14, 12, 0),
                        Arrays.asList(
                                new TicketCategory(
                                        1,
                                        "Standard",
                                        12
                                ),
                                new TicketCategory(
                                        2,
                                        "VIP",
                                        24
                                )
                        )
                ),
                new Event(
                        2,
                        new Venue(
                                2,
                                "Cluj",
                                "Venue",
                                2000
                        ),
                        "Concert",
                        "Let's dance in the rain ;)",
                        "Electric Castle",
                        "https://electriccastle.ro/assets/img/ec-official-logo.jpg",
                        LocalDateTime.of(2023, 9, 10, 12, 0),
                        LocalDateTime.of(2023, 9, 14, 12, 0),
                        Arrays.asList(
                                new TicketCategory(
                                        3,
                                        "Standard",
                                        200
                                ),
                                new TicketCategory(
                                        4,
                                        "VIP",
                                        400
                                )
                        )
                )
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar materialToolbar = findViewById(R.id.mainToolbar);
        materialToolbar.setOnMenuItemClickListener(item -> {
            int menuId = item.getItemId();

            if (menuId == R.id.main_filter) {
                FilterModalBottomSheet filterModalBottomSheet = new FilterModalBottomSheet();
                filterModalBottomSheet.show(getSupportFragmentManager(), FilterModalBottomSheet.TAG);

                return true;
            } else if (menuId == R.id.main_sort) {
                SortModalBottomSheet sortModalBottomSheet = new SortModalBottomSheet();
                sortModalBottomSheet.show(getSupportFragmentManager(), SortModalBottomSheet.TAG);

                return true;
            } else {
                return false;
            }
        });

        RecyclerView eventsRecyclerView = findViewById(R.id.eventRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        eventsRecyclerView.setLayoutManager(layoutManager);
        EventAdapter adapter = new EventAdapter(getMockEvents());
        eventsRecyclerView.setAdapter(adapter);

        FloatingActionButton orderFab = findViewById(R.id.ordersFab);
        orderFab.setOnClickListener(view -> {
            Intent intent = new Intent(this, OrdersActivity.class);
            startActivity(intent);
        });
    }
}
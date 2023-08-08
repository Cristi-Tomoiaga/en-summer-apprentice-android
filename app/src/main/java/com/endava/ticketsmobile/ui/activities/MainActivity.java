package com.endava.ticketsmobile.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.endava.ticketsmobile.R;
import com.endava.ticketsmobile.data.model.Event;
import com.endava.ticketsmobile.data.services.TicketsJavaService;
import com.endava.ticketsmobile.data.services.util.TicketsServiceFactory;
import com.endava.ticketsmobile.ui.adapters.EventAdapter;
import com.endava.ticketsmobile.ui.fragments.EventSortCriteria;
import com.endava.ticketsmobile.ui.fragments.FilterModalBottomSheet;
import com.endava.ticketsmobile.ui.fragments.SortModalBottomSheet;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EventAdapter adapter;
    private String venueFilter;
    private String eventNameFilter;
    private String eventTypeFilter;
    private EventSortCriteria criteria = EventSortCriteria.NONE;

    private void setupToolbar() {
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
    }

    private void setupRecyclerView() {
        RecyclerView eventsRecyclerView = findViewById(R.id.eventRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        eventsRecyclerView.setLayoutManager(layoutManager);

        adapter = new EventAdapter();
        eventsRecyclerView.setAdapter(adapter);

        TicketsJavaService ticketsJavaService = TicketsServiceFactory.createTicketServiceForJava();
        Call<List<Event>> eventsGetCall = ticketsJavaService.getEvents();
        eventsGetCall.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(@NonNull Call<List<Event>> call, @NonNull Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    adapter.updateData(response.body());
                } else {
                    Toast.makeText(getApplicationContext(), "Error " + response.message(), Toast.LENGTH_SHORT)
                            .show();
                    Log.d("NetworkRequest", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Event>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Error " + t.getMessage(), Toast.LENGTH_SHORT)
                     .show();
                Log.e("NetworkRequest", t.getMessage());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupRecyclerView();

        FloatingActionButton orderFab = findViewById(R.id.ordersFab);
        orderFab.setOnClickListener(view -> {
            Intent intent = new Intent(this, OrdersActivity.class);
            startActivity(intent);
        });
    }

    public void filterEvents(String eventName, String venue, String eventType) {
        eventNameFilter = eventName;
        eventTypeFilter = eventType;
        venueFilter = venue;
        criteria = EventSortCriteria.NONE;

        TicketsJavaService ticketsJavaService = TicketsServiceFactory.createTicketServiceForJava();
        Call<List<Event>> eventsGetCall;

        if (!"".equals(eventType) && !"".equals(venue)) {
            eventsGetCall = ticketsJavaService.getEventsByVenueAndType(venue, eventType);
        } else if (!"".equals(eventType)) {
            eventsGetCall = ticketsJavaService.getEventsByType(eventType);
        } else if (!"".equals(venue)) {
            eventsGetCall = ticketsJavaService.getEventsByVenue(venue);
        } else {
            eventsGetCall = ticketsJavaService.getEvents();
        }

        eventsGetCall.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(@NonNull Call<List<Event>> call, @NonNull Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Event> filteredEvents = response
                                .body()
                                .stream()
                                .filter(e -> e.getName().contains(eventName))
                                .collect(Collectors.toList());

                        adapter.updateData(filteredEvents);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error " + response.message(), Toast.LENGTH_SHORT)
                            .show();
                    Log.d("NetworkRequest", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Event>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Error " + t.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                Log.e("NetworkRequest", t.getMessage());
            }
        });
    }

    public void sortEvents(EventSortCriteria criteria) {
        this.criteria = criteria;

        List<Event> sortedEvents = adapter.getData();
        switch (criteria) {
            case NAME_ASC:
                sortedEvents.sort(Comparator.comparing(Event::getName));
                break;
            case NAME_DESC:
                sortedEvents.sort(Collections.reverseOrder(Comparator.comparing(Event::getName)));
                break;
            case DATE_ASC:
                sortedEvents.sort(Comparator.comparing(Event::getStartDate));
                break;
            case DATE_DESC:
                sortedEvents.sort(Collections.reverseOrder(Comparator.comparing(Event::getStartDate)));
                break;
            case NONE:
                break;
        }

        adapter.updateData(sortedEvents);
    }

    public String getVenueFilter() {
        return venueFilter;
    }

    public String getEventNameFilter() {
        return eventNameFilter;
    }

    public String getEventTypeFilter() {
        return eventTypeFilter;
    }

    public EventSortCriteria getCriteria() {
        return criteria;
    }
}
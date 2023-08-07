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
import com.endava.ticketsmobile.ui.fragments.FilterModalBottomSheet;
import com.endava.ticketsmobile.ui.fragments.SortModalBottomSheet;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

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

        EventAdapter adapter = new EventAdapter();
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
}
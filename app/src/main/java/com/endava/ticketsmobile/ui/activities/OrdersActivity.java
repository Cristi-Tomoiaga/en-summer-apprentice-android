package com.endava.ticketsmobile.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.endava.ticketsmobile.R;
import com.endava.ticketsmobile.data.model.Event;
import com.endava.ticketsmobile.data.model.Order;
import com.endava.ticketsmobile.ui.adapters.EventAdapter;
import com.endava.ticketsmobile.ui.adapters.OrderAdapter;
import com.endava.ticketsmobile.ui.fragments.FilterModalBottomSheet;
import com.endava.ticketsmobile.ui.fragments.OrderSortModalBottomSheet;
import com.endava.ticketsmobile.ui.fragments.SortModalBottomSheet;
import com.google.android.material.appbar.MaterialToolbar;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    public static List<Order> getMockOrders() {
        List<Event> events = MainActivity.getMockEvents();

        return Arrays.asList(
                new Order(
                        1,
                        events.get(0),
                        LocalDateTime.of(2023, 7, 1, 13, 0),
                        events.get(0).getTicketCategories().get(0),
                        10,
                        120.0
                ),
                new Order(
                        2,
                        events.get(1),
                        LocalDateTime.of(2023, 7, 2, 12, 0),
                        events.get(1).getTicketCategories().get(1),
                        3,
                        1200.0
                )
        );
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        MaterialToolbar toolbar = findViewById(R.id.orderToolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        toolbar.setOnMenuItemClickListener(item -> {
            int menuId = item.getItemId();

            if (menuId == R.id.main_sort) {
                OrderSortModalBottomSheet sortModalBottomSheet = new OrderSortModalBottomSheet();
                sortModalBottomSheet.show(getSupportFragmentManager(), OrderSortModalBottomSheet.TAG);

                return true;
            } else {
                return false;
            }
        });

        RecyclerView orderRecyclerView = findViewById(R.id.orderRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        orderRecyclerView.setLayoutManager(layoutManager);
        OrderAdapter adapter = new OrderAdapter(getMockOrders());
        orderRecyclerView.setAdapter(adapter);
    }
}
package com.endava.ticketsmobile.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.endava.ticketsmobile.R;
import com.endava.ticketsmobile.data.model.Order;
import com.endava.ticketsmobile.data.services.TicketsJavaService;
import com.endava.ticketsmobile.data.services.util.TicketsServiceFactory;
import com.endava.ticketsmobile.ui.adapters.OrderAdapter;
import com.endava.ticketsmobile.ui.fragments.OrderSortModalBottomSheet;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity {

    private void setupToolbar() {
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
    }

    private void setupRecyclerView() {
        RecyclerView orderRecyclerView = findViewById(R.id.orderRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        orderRecyclerView.setLayoutManager(layoutManager);
        OrderAdapter adapter = new OrderAdapter();
        orderRecyclerView.setAdapter(adapter);

        TicketsJavaService ticketsJavaService = TicketsServiceFactory.createTicketServiceForJava();
        Call<List<Order>> ordersGetCall = ticketsJavaService.getOrders();
        ordersGetCall.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(@NonNull Call<List<Order>> call, @NonNull Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    adapter.updateData(response.body());
                } else {
                    Toast.makeText(getApplicationContext(), "Error " + response.message(), Toast.LENGTH_SHORT)
                            .show();
                    Log.d("NetworkRequest", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Order>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Error " + t.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                Log.e("NetworkRequest", t.getMessage());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        setupToolbar();
        setupRecyclerView();
    }
}
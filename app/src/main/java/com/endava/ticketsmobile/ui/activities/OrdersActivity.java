package com.endava.ticketsmobile.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.endava.ticketsmobile.R;
import com.endava.ticketsmobile.data.model.Order;
import com.endava.ticketsmobile.data.services.TicketsJavaService;
import com.endava.ticketsmobile.data.services.util.TicketsErrorHandler;
import com.endava.ticketsmobile.data.services.util.TicketsServiceFactory;
import com.endava.ticketsmobile.ui.adapters.OrderAdapter;
import com.endava.ticketsmobile.ui.fragments.OrderSortCriteria;
import com.endava.ticketsmobile.ui.fragments.OrderSortModalBottomSheet;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity {
    private OrderAdapter adapter;
    private OrderSortCriteria criteria = OrderSortCriteria.NONE;

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
        adapter = new OrderAdapter();
        orderRecyclerView.setAdapter(adapter);

        TicketsJavaService ticketsJavaService = TicketsServiceFactory.createTicketServiceForJava();
        Call<List<Order>> ordersGetCall = ticketsJavaService.getOrders();
        ordersGetCall.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(@NonNull Call<List<Order>> call, @NonNull Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    adapter.updateData(response.body());
                } else {
                    String errorMessage = TicketsErrorHandler.getErrorMessageFromResponse(response);
                    if (errorMessage != null) {
                        Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT)
                                .show();
                        Log.d("NetworkRequest", errorMessage);
                    } else {
                        Toast.makeText(getApplicationContext(), "Error " + response.message(), Toast.LENGTH_SHORT)
                                .show();
                        Log.d("NetworkRequest", response.message());
                    }
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

    private void setupSwipeRefreshLayout() {
        SwipeRefreshLayout orderSwipeRefreshLayout = findViewById(R.id.orderSwipeRefreshLayout);
        orderSwipeRefreshLayout.setOnRefreshListener(() -> {
            criteria = OrderSortCriteria.NONE;

            TicketsJavaService ticketsJavaService = TicketsServiceFactory.createTicketServiceForJava();
            Call<List<Order>> ordersGetCall = ticketsJavaService.getOrders();
            ordersGetCall.enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(@NonNull Call<List<Order>> call, @NonNull Response<List<Order>> response) {
                    if (response.isSuccessful()) {
                        adapter.updateData(response.body());
                    } else {
                        String errorMessage = TicketsErrorHandler.getErrorMessageFromResponse(response);
                        if (errorMessage != null) {
                            Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT)
                                    .show();
                            Log.d("NetworkRequest", errorMessage);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error " + response.message(), Toast.LENGTH_SHORT)
                                    .show();
                            Log.d("NetworkRequest", response.message());
                        }
                    }
                    orderSwipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(@NonNull Call<List<Order>> call, @NonNull Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error " + t.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                    Log.e("NetworkRequest", t.getMessage());
                    orderSwipeRefreshLayout.setRefreshing(false);
                }
            });
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        setupToolbar();
        setupRecyclerView();
        setupSwipeRefreshLayout();
    }

    public void sortOrders(OrderSortCriteria criteria) {
        this.criteria = criteria;

        List<Order> sortedOrders = adapter.getData();
        switch (criteria) {
            case DATE_ASC:
                sortedOrders.sort(Comparator.comparing(Order::getTimestamp));
                break;
            case DATE_DESC:
                sortedOrders.sort(Collections.reverseOrder(Comparator.comparing(Order::getTimestamp)));
                break;
            case PRICE_ASC:
                sortedOrders.sort(Comparator.comparing(Order::getTotalPrice));
                break;
            case PRICE_DESC:
                sortedOrders.sort(Collections.reverseOrder(Comparator.comparing(Order::getTotalPrice)));
                break;
            case NONE:
                break;
        }

        adapter.updateData(sortedOrders);
    }

    public OrderSortCriteria getCriteria() {
        return criteria;
    }
}
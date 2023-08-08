package com.endava.ticketsmobile.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.endava.ticketsmobile.R;
import com.endava.ticketsmobile.ui.activities.OrdersActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class OrderSortModalBottomSheet extends BottomSheetDialogFragment {
    public static final String TAG = "OrderSortModalBottomSheet";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_sort_bottom_sheet, container, false);

        Chip chipDateAsc = view.findViewById(R.id.orderChipDateAsc);
        Chip chipDateDesc = view.findViewById(R.id.orderChipDateDesc);
        Chip chipPriceAsc = view.findViewById(R.id.orderChipPriceAsc);
        Chip chipPriceDesc = view.findViewById(R.id.orderChipPriceDesc);

        OrdersActivity ordersActivity = (OrdersActivity) requireActivity();
        switch (ordersActivity.getCriteria()) {
            case DATE_ASC:
                chipDateAsc.setChecked(true);
                break;
            case DATE_DESC:
                chipDateDesc.setChecked(true);
                break;
            case PRICE_ASC:
                chipPriceAsc.setChecked(true);
                break;
            case PRICE_DESC:
                chipPriceDesc.setChecked(true);
                break;
            case NONE:
                chipDateAsc.setChecked(false);
                chipDateDesc.setChecked(false);
                chipPriceAsc.setChecked(false);
                chipPriceDesc.setChecked(false);
                break;
        }

        ChipGroup orderSortChipGroup = view.findViewById(R.id.orderSortChipGroup);
        orderSortChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            int selectedChipId = group.getCheckedChipId();

            if (selectedChipId == R.id.orderChipDateAsc) {
                ordersActivity.sortOrders(OrderSortCriteria.DATE_ASC);
            } else if (selectedChipId == R.id.orderChipDateDesc) {
                ordersActivity.sortOrders(OrderSortCriteria.DATE_DESC);
            } else if (selectedChipId == R.id.orderChipPriceAsc) {
                ordersActivity.sortOrders(OrderSortCriteria.PRICE_ASC);
            } else if (selectedChipId == R.id.orderChipPriceDesc) {
                ordersActivity.sortOrders(OrderSortCriteria.PRICE_DESC);
            } else {
                ordersActivity.sortOrders(OrderSortCriteria.NONE);
            }
            dismiss();
        });

        return view;
    }
}

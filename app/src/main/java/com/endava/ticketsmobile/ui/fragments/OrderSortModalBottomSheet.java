package com.endava.ticketsmobile.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.endava.ticketsmobile.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class OrderSortModalBottomSheet extends BottomSheetDialogFragment {
    public static final String TAG = "OrderSortModalBottomSheet";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_sort_bottom_sheet, container, false);
    }
}

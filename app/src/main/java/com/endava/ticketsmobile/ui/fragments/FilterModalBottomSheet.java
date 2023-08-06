package com.endava.ticketsmobile.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.endava.ticketsmobile.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FilterModalBottomSheet extends BottomSheetDialogFragment {
    public static final String TAG = "FilterModalBottomSheet";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filter_bottom_sheet, container, false);
    }
}

package com.endava.ticketsmobile.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.endava.ticketsmobile.R;
import com.endava.ticketsmobile.ui.activities.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class SortModalBottomSheet extends BottomSheetDialogFragment {
    public static final String TAG = "SortModalBottomSheet";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sort_bottom_sheet, container, false);

        Chip chipNameAsc = view.findViewById(R.id.chipNameAsc);
        Chip chipNameDesc = view.findViewById(R.id.chipNameDesc);
        Chip chipDateAsc = view.findViewById(R.id.chipDateAsc);
        Chip chipDateDesc = view.findViewById(R.id.chipDateDesc);

        MainActivity mainActivity = (MainActivity) requireActivity();
        switch (mainActivity.getCriteria()) {
            case NAME_ASC:
                chipNameAsc.setChecked(true);
                break;
            case NAME_DESC:
                chipNameDesc.setChecked(true);
                break;
            case DATE_ASC:
                chipDateAsc.setChecked(true);
                break;
            case DATE_DESC:
                chipDateDesc.setChecked(true);
                break;
            case NONE:
                chipNameAsc.setChecked(false);
                chipNameDesc.setChecked(false);
                chipDateAsc.setChecked(false);
                chipDateDesc.setChecked(false);
                break;
        }

        ChipGroup mainSortChipGroup = view.findViewById(R.id.mainSortChipGroup);
        mainSortChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            int selectedChipId = group.getCheckedChipId();

            if (selectedChipId == R.id.chipNameAsc) {
                mainActivity.sortEvents(EventSortCriteria.NAME_ASC);
            } else if (selectedChipId == R.id.chipNameDesc) {
                mainActivity.sortEvents(EventSortCriteria.NAME_DESC);
            } else if (selectedChipId == R.id.chipDateAsc) {
                mainActivity.sortEvents(EventSortCriteria.DATE_ASC);
            } else if (selectedChipId == R.id.chipDateDesc) {
                mainActivity.sortEvents(EventSortCriteria.DATE_DESC);
            } else {
                mainActivity.sortEvents(EventSortCriteria.NONE);
            }
            dismiss();
        });

        return view;
    }
}

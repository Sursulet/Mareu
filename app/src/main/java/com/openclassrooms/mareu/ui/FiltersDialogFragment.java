package com.openclassrooms.mareu.ui;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.di.DI;
import com.openclassrooms.mareu.model.RoomItem;
import com.openclassrooms.mareu.service.MeetingApiService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FiltersDialogFragment extends DialogFragment {

    private MeetingApiService service;
    private FragmentManager manager;
    private ListMeetingActivity activity;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.filters_date) TextInputEditText mDate;
    @BindView(R.id.rooms) ChipGroup mRoomsList;

    Chip chip;
    private String typeFilter, chipString;

    private Callback mCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_DialogWhenLarge);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_filters, container, false);
        ButterKnife.bind(this, view);

        service = DI.getNewInstanceApiService();
        manager = getFragmentManager();
        activity = (ListMeetingActivity) getActivity();

        buildToolbar();
        buildChipGroup();

        mDate.setOnClickListener(v -> {
            typeFilter = "days";
            if(chip != null) chip.setCheckable(false);
            showDatePickerDialog();
        });

        mRoomsList.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId < 0) { mCallback.onActionFilter("", ""); return; }
            chip = group.findViewById(checkedId);
            if(chip.isCheckable()) {
                typeFilter = "rooms";
                chipString = chip.getText().toString();
            }
        });

        return view;
    }

    private void buildToolbar() {
        mToolbar.inflateMenu(R.menu.menu_dialog_fragment);
        mToolbar.setNavigationIcon(R.drawable.ic_close);
        mToolbar.setTitle("Filters");
        mToolbar.setNavigationOnClickListener(v -> {dismiss();});
        mToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_ok) {
                if(typeFilter == null) return false;
                String constraint ="";
                if(typeFilter.equals("days") && mDate.getText() != null) constraint = mDate.getText().toString();
                if(typeFilter.equals("rooms")) constraint = chipString;
                mCallback.onActionFilter(typeFilter, constraint);
                dismiss();
                return true;
            }
            return false;
        });
    }

    public interface Callback {
        void onActionFilter(String typeFilter, String constraint);
    }

    void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(manager, "datePickerFilters");
    }

    private void buildChipGroup() {
        List<RoomItem> ROOMS = service.getRooms();
        for(RoomItem r : ROOMS) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip_room, mRoomsList, false);
            chip.setText(r.getName());
            chip.setChipIconTint(ColorStateList.valueOf(getResources().getColor(r.getImgColor())));
            mRoomsList.addView(chip);
        }
    }
}

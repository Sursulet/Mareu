package com.openclassrooms.mareu.ui;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FiltersDialogFragment extends DialogFragment {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private MeetingApiService service;
    private FragmentManager manager;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.filters_date) TextInputEditText mDate;
    @BindView(R.id.rooms) ChipGroup mRoomsList;

    private Chip chip;
    private String typeFilter;

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

        buildToolbar();
        buildChipGroup();

        mDate.setOnClickListener(v -> {
            if(chip != null) mRoomsList.clearCheck();
            typeFilter="days";
            showDatePickerDialog();
        });

        mRoomsList.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId < 0) { typeFilter = null; return; }
            chip = group.findViewById(checkedId);
            if(chip.isCheckable()) {mDate.setText(""); typeFilter="rooms";}
        });

        return view;
    }

    private void buildToolbar() {
        mToolbar.inflateMenu(R.menu.menu_dialog_fragment);
        mToolbar.setNavigationIcon(R.drawable.ic_close);
        mToolbar.setTitle("Filters");
        mToolbar.setNavigationOnClickListener(v -> dismiss());
        mToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_ok) {
                String constraint = ""; String mDateString="";
                if(mDate.getText() != null) {mDateString = mDate.getText().toString(); typeFilter="days";}
                if(typeFilter.equals("days")) constraint = mDateString;
                if(typeFilter.equals("rooms")) {constraint = chip.getText().toString(); typeFilter="rooms";}
                if(mDate.getText().toString().isEmpty() && chip == null) return false;

                if(mCallback == null) return false;
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
        DialogFragment newFragment = new DatePickerFragment(mDateSetListener);
        newFragment.show(manager, "datePickerFilters");
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.FRANCE);
            String mDateString = simpleDateFormat.format(c.getTime());
            mDate.setText(mDateString);
        }
    };

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

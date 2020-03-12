package com.openclassrooms.mareu.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.model.RoomItem;
import com.openclassrooms.mareu.service.MeetingApiService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.openclassrooms.mareu.service.DummyRoomGenerator.DUMMY_ROOMS;

public class NewMeetingDialogFragment extends DialogFragment implements View.OnClickListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.topicInputLayout) TextInputLayout mTopicInputLayout;
    @BindView(R.id.topicEditText) TextInputEditText mTopicEditText;
    @BindView(R.id.dateInputLayout) TextInputLayout mDateInputLayout;
    @BindView(R.id.dateEditText) TextInputEditText mDateEditText;
    @BindView(R.id.timeInputLayout) TextInputLayout mTimeInputLayout;
    @BindView(R.id.timeEditText) TextInputEditText mTimeEditText;
    @BindView(R.id.roomsSpinner) Spinner mRoomsSpinner;
    @BindView(R.id.emailInputLayout) TextInputLayout mEmailInputLayout;
    @BindView(R.id.emailEditText) TextInputEditText mEmailEditText;
    @BindView(R.id.add_email_btn) ImageButton mAddEmail;
    @BindView(R.id.emailGrp) ChipGroup mEmailList;

    private Context context;
    private FragmentManager manager;
    private MeetingApiService service;
    private ListMeetingActivity activity;

    private String mTopicString, mDateString, mTimeString, mRoomString, mEmailString;

    private ArrayList<String> EMAILS = new ArrayList<>();
    private Date mToday;
    Date mDatePicker;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_DialogWhenLarge);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_new_meeting, container, false);
        ButterKnife.bind(this, view);

        activity = (ListMeetingActivity) getActivity();
        if (activity != null) { service = activity.getService(); }
        context = getContext();
        manager = getFragmentManager();

        buildToolbar();
        getTodayDate();
        mDateEditText.setOnClickListener(this);
        mTimeEditText.setOnClickListener(this);
        buildRoomListSpinner();
        mAddEmail.setOnClickListener(this);

        return view;
    }

    private void getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        mToday = calendar.getTime();
    }

    private void buildToolbar() {
        mToolbar.inflateMenu(R.menu.menu_dialog_fragment);
        mToolbar.setNavigationIcon(R.drawable.ic_close);
        mToolbar.setTitle("New Meeting");
        mToolbar.setNavigationOnClickListener(v -> dismiss());
        mToolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.action_ok) { return confirmForm(); }
            return false;
        });
    }

    private void buildRoomListSpinner() {
        ArrayList<RoomItem> ROOMS = new ArrayList<>(DUMMY_ROOMS);
        mRoomsSpinner.setAdapter(new RoomItemAdapter(context, ROOMS));
        mRoomsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RoomItem clickedItem = (RoomItem) parent.getItemAtPosition(position);
                mRoomString = clickedItem.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.dateEditText) {showDatePickerDialog();}
        if(id == R.id.timeEditText) {showTimePickerDialog();}
        if(id == R.id.add_email_btn) {
            if(!validateEmail()) return;
            if(mEmailEditText.getText() != null) { mEmailString = mEmailEditText.getText().toString(); }
            EMAILS.add(mEmailString);
            buildChip();

            mEmailEditText.getText().clear();
        }
    }

    private void buildChip() {
        final View rowView = View.inflate(getContext(), R.layout.item_chip, null);
        Chip chip = rowView.findViewById(R.id.item_chip);
        chip.setText(mEmailEditText.getText());
        chip.setOnCloseIconClickListener(v -> {
            mEmailList.removeView(v);
            EMAILS.remove(chip.getText().toString());
        });

        mEmailList.addView(rowView);
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(manager, "datePicker");
    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(manager, "timePicker");
    }

    private boolean confirmForm() {
        if(!validateTopic() | !validateDate() | ! validateTime() | !validateEmailList()) { return false; }
        Meeting meeting = new Meeting(mTopicString, mDateString, mTimeString, mRoomString, EMAILS);
        if (activity != null) activity.onAddMeeting(meeting);

        dismiss();
        return true;
    }

    private boolean validateTopic() {

        if(mTopicEditText.getText() != null) { mTopicString = mTopicEditText.getText().toString(); }
        if(mTopicString.trim().isEmpty()) {
            mTopicInputLayout.setErrorEnabled(true);
            mTopicInputLayout.setError("Please enter a topic");
            return false;
        }

        mTopicInputLayout.setErrorEnabled(false);
        return true;
    }

    private boolean validateDate() {
        if(mDateEditText.getText() != null) { mDateString = mDateEditText.getText().toString(); }
        if(mDateString.trim().isEmpty()) {
            mDateInputLayout.setErrorEnabled(true);
            mDateInputLayout.setError("Please enter a date");
            return false;
        }
        if(mDatePicker.compareTo(mToday) < 0) {
            mDateInputLayout.setErrorEnabled(true);
            mDateInputLayout.setError("Valid date required");
            return false;
        }

        mDateInputLayout.setErrorEnabled(false);
        return true;
    }

    private boolean validateTime() {

        if(mTimeEditText.getText() != null) { mTimeString = mTimeEditText.getText().toString(); }
        if(mTimeString.trim().isEmpty()) {
            mTimeInputLayout.setErrorEnabled(true);
            mTimeInputLayout.setError("Please enter a time");
            return false;
        }
        if(!service.compareTime(mDateString, mTimeString)) {
            mTimeInputLayout.setErrorEnabled(true);
            mTimeInputLayout.setError("Please enter a time");
            return false;
        }

        mTimeInputLayout.setErrorEnabled(false);
        return true;
    }

    private boolean validateEmail() {
        if(mEmailEditText.getText() != null) { mEmailString = mEmailEditText.getText().toString(); }
        if(mEmailString.isEmpty()) {
            mEmailInputLayout.setErrorEnabled(true);
            mEmailInputLayout.setError("Please, enter a mail");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(mEmailString).matches()) {
            mEmailInputLayout.setErrorEnabled(true);
            mEmailInputLayout.setError("Valid email required");
            return false;
        } else if(EMAILS.contains(mEmailString)) {
            mEmailInputLayout.setErrorEnabled(true);
            mEmailInputLayout.setError("Email is already in use");
            return false;
        }

        mEmailInputLayout.setErrorEnabled(false);
        return true;
    }

    private boolean validateEmailList() {
        if(EMAILS.size() == 0) {
            mEmailInputLayout.setErrorEnabled(true);
            mEmailInputLayout.setError("Please, enter a mail");
            return false;
        }

        mEmailInputLayout.setErrorEnabled(false);
        return true;
    }

}

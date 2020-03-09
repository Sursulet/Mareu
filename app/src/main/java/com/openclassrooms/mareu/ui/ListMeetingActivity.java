package com.openclassrooms.mareu.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.di.DI;
import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.service.MeetingApiService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListMeetingActivity extends AppCompatActivity implements MyMeetingRecyclerViewAdapter.OnMeetingListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private MeetingApiService service;
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.main_recyclerView) RecyclerView mRecyclerView;

    private List<Meeting> mMeetings;
    private MyMeetingRecyclerViewAdapter mAdapter;
    private ActionMode mActionMode;

    NewMeetingDialogFragment mNewMeetingDialog;
    FiltersDialogFragment mFiltersDialog;
    String currentDateString, currentTimeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        service = DI.getNewInstanceApiService();
        setSupportActionBar(toolbar);

        fab.setOnClickListener(view -> showNewMeetingDialog());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        buildRecyclerView();
    }

    public void getMeetingFilter(String typeFilter, String constraint) {
        mAdapter.getFilterItem(typeFilter);
        mAdapter.getFilter().filter(constraint);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filters) {
            showFiltersDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void buildRecyclerView() {
        mMeetings = service.getMeetings();//new ArrayList<>(DUMMY_MEETINGS);
        mAdapter = new MyMeetingRecyclerViewAdapter(ListMeetingActivity.this, mMeetings, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showNewMeetingDialog() {
        mNewMeetingDialog = new NewMeetingDialogFragment();
        mNewMeetingDialog.show(getSupportFragmentManager(), "NewMeetingDialogFragment");
    }

    private void showFiltersDialog() {
        mFiltersDialog = new FiltersDialogFragment();
        mFiltersDialog.setCallback((typeFilter, constraint) -> {
            mAdapter.getFilterItem(typeFilter);
            mAdapter.getFilter().filter(constraint);

            if(mActionMode != null) { return; }
            mActionMode = startActionMode(mActionModeCallback);
        });
        mFiltersDialog.show(getSupportFragmentManager(), "FiltersDialogFragment");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onDeleteMeeting(int position) {
        if(mActionMode == null) {
            service.deleteMeeting(mMeetings.get(position));
            buildRecyclerView();
        }
    }

    public void onAddMeeting(Meeting m) {
        if(mActionMode == null) {
            service.addMeeting(m);
            buildRecyclerView();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.FRANCE);
        currentDateString = simpleDateFormat.format(c.getTime());

        if(mNewMeetingDialog != null && mNewMeetingDialog.isVisible()) {
            mNewMeetingDialog.mDateEditText.setText(currentDateString);
            mNewMeetingDialog.mDatePicker = c.getTime();
        }
        if(mFiltersDialog != null && mFiltersDialog.isVisible()) mFiltersDialog.mDate.setText(currentDateString);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        currentTimeString = String.format(Locale.FRANCE,"%d:%d", hourOfDay, minute);
        mNewMeetingDialog.mTimeEditText.setText(currentTimeString);
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Filter");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.getFilter().filter("");
            mActionMode = null;
        }
    };
}

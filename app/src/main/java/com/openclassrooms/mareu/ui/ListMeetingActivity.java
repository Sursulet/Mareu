package com.openclassrooms.mareu.ui;

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
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListMeetingActivity extends AppCompatActivity implements MyMeetingRecyclerViewAdapter.OnMeetingListener {

    private MeetingApiService service;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.main_recyclerView) RecyclerView mRecyclerView;

    private List<Meeting> mMeetings;
    private MyMeetingRecyclerViewAdapter mAdapter;
    private ActionMode mActionMode;

    NewMeetingDialogFragment mNewMeetingDialog;
    FiltersDialogFragment mFiltersDialog;

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

    public MeetingApiService getService() { return service; }

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
            List<Meeting> mFilterList = new ArrayList<>();
            if(typeFilter.equals("")) { mFilterList.addAll(mMeetings); }
            if(typeFilter.equals("days")) mFilterList = service.getFilterByDay(constraint);
            if(typeFilter.equals("rooms")) mFilterList = service.getFilterByRoom(constraint);
            updateRecyclerView(mFilterList);

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

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Filter");
            fab.setVisibility(View.INVISIBLE);
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
            updateRecyclerView(mMeetings);
            mActionMode = null;
            fab.setVisibility(View.VISIBLE);
        }
    };

    public void updateRecyclerView(List<Meeting> meetings) {
        mAdapter = new MyMeetingRecyclerViewAdapter(ListMeetingActivity.this, meetings, this);
        mRecyclerView.setAdapter(mAdapter);
    }
}

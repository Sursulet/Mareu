package com.openclassrooms.mareu.service;

import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.model.RoomItem;

import java.util.ArrayList;
import java.util.List;

public class DummyMeetingApiService implements MeetingApiService {

    //private List<Meeting> meetings = new ArrayList<>();
    private List<Meeting> meetings = DummyMeetingGenerator.generateMeetings();
    private List<RoomItem> rooms = DummyRoomGenerator.generateRooms();

    @Override
    public List<Meeting> getMeetings() { return meetings; }

    @Override
    public List<RoomItem> getRooms() { return rooms; }

    @Override
    public void addMeeting(Meeting meeting) { meetings.add(meeting); }

    @Override
    public void deleteMeeting(Meeting meeting) { meetings.remove(meeting); }
}

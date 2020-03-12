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

    @Override
    public boolean compareTime(String date, String time) {
        for(Meeting m : meetings) {
            if(m.getDate().contains(date) && m.getTime().contains(time)) return false;
        }
        return true;
    }

    @Override
    public List<Meeting> getFilterByDay(String constraint) {
        List<Meeting> filteredList = new ArrayList<>();
        if(constraint == null || constraint.length() == 0) { filteredList.addAll(meetings); }
        else {
            String filterPattern = constraint.toLowerCase().trim();
            for (Meeting m : meetings) {
                if(m.getDate().toLowerCase().contains(filterPattern)) {filteredList.add(m);}
            }
        }

        return filteredList;
    }

    @Override
    public List<Meeting> getFilterByRoom(String constraint) {
        List<Meeting> filteredList = new ArrayList<>();
        if(constraint == null || constraint.length() == 0) { filteredList.addAll(meetings); }
        else {
            String filterPattern = constraint.toLowerCase().trim();
            for (Meeting m : meetings) {
                if(m.getRoom().toLowerCase().contains(filterPattern)) {filteredList.add(m);}
            }
        }

        return filteredList;
    }
}
